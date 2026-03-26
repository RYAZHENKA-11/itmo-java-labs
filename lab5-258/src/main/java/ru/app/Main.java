package ru.app;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.history.DefaultHistory;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import ru.app.collection.Collection;
import ru.app.command.Command;
import ru.app.command.ExitCommand;
import ru.app.invoker.Invoker;
import ru.app.io.ConsoleReader;
import ru.app.io.JLineHistory;
import ru.app.json.Json;
import ru.app.object.Product;
import ru.app.parser.CommandParser;

import java.io.*;
import java.nio.file.Path;
import java.util.PriorityQueue;

public class Main {
  private static Collection collection;
  private static File dataFile;

  public static void main(String[] args) {
    initializeCollection(args);

    Terminal terminal;
    LineReader reader;
    PrintWriter out;
    ConsoleReader consoleReader;
    Invoker invoker;
    CommandParser parser;
    JLineHistory history;

    try {
      terminal = TerminalBuilder.builder()
          .system(true)
          .build();

      Path historyPath = dataFile != null ? dataFile.toPath() : Path.of(".");
      history = new JLineHistory(historyPath);

      DefaultHistory jlineHistory = history.getHistory();

      reader = LineReaderBuilder.builder()
          .terminal(terminal)
          .history(jlineHistory)
          .build();

      out = terminal.writer();
      consoleReader = new ConsoleReader(out, collection);
      invoker = new Invoker();
      invoker.setHistory(history.getHistoryList());
      parser = new CommandParser(collection, out, terminal, invoker, dataFile, consoleReader);

      out.println("Application started. Type 'help' for available commands.");
      while (true) {
        String line = null;
        try {
          line = reader.readLine("> ");
          if (line == null || line.trim().isEmpty()) {
            continue;
          }

          Command command = parser.parse(line);
          invoker.execute(command);

          if (command instanceof ExitCommand) {
            break;
          }

          String commandName = JLineHistory.extractCommandName(line);
          if (commandName != null) {
            history.add(commandName);
            history.save();
          }
        } catch (IllegalArgumentException e) {
          out.println("Error: " + e.getMessage());
        } catch (Exception e) {
          out.println("Unexpected error: " + e.getMessage());
        }
      }

      out.println("Application finished.");
    } catch (Exception e) {
      System.err.println("Terminal error: " + e.getMessage());
    }
  }

  private static void initializeCollection(String[] args) {
    if (args == null || args.length == 0) {
      System.err.println("No file name provided.");
      System.out.println("Initializing with an empty collection...");
      collection = new Collection();
    } else if (args.length > 1) {
      System.err.println("File name provided incorrectly.");
      System.out.println("Initializing with an empty collection...");
      collection = new Collection();
    } else if (args[0] == null || !args[0].endsWith(".json")) {
      System.err.println("File name must be .json");
      System.out.println("Initializing with an empty collection...");
      collection = new Collection();
    } else {
      String fileName = args[0];
      dataFile = new File(fileName);
      try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(dataFile))) {
        PriorityQueue<Product> products = Json.readProducts(bis);
        collection = new Collection();
        for (Product p : products) {
          try {
            collection.add(p);
          } catch (IllegalArgumentException e) {
            System.err.println(
                "Warning: product with id="
                    + p.id()
                    + " is not unique and was skipped: "
                    + e.getMessage());
          }
        }
      } catch (FileNotFoundException e) {
        System.err.println("File not found.");
        System.out.println("Initializing with an empty collection...");
        collection = new Collection();
      } catch (IOException e) {
        System.err.println("Read error: " + e.getMessage());
        System.out.println("Initializing with an empty collection...");
        collection = new Collection();
      } catch (SecurityException e) {
        System.err.println("Access denied: " + e.getMessage());
        System.out.println("Initializing with an empty collection...");
        collection = new Collection();
      }
    }
  }
}