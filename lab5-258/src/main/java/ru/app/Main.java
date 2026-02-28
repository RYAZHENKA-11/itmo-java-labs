package ru.app;

import ru.app.collection.Collection;
import ru.app.command.Command;
import ru.app.command.ExitCommand;
import ru.app.invoker.Invoker;
import ru.app.io.ConsoleReader;
import ru.app.json.Json;
import ru.app.object.Product;
import ru.app.parser.CommandParser;
import ru.app.source.CommandSource;
import ru.app.source.ConsoleSource;

import java.io.*;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * Главный класс приложения. Инициализирует коллекцию, парсер, инвокер и запускает цикл обработки
 * команд.
 */
public class Main {
  private static Collection collection;
  private static File dataFile; // файл для save (может быть null)

  public static void main(String[] args) {
    // 1. Обработка аргументов командной строки и инициализация коллекции
    initializeCollection(args);

    // 2. Создание основных объектов
    Scanner consoleScanner = new Scanner(System.in);
    PrintWriter out = new PrintWriter(System.out, true);
    ConsoleReader consoleReader = new ConsoleReader(out, collection);
    Invoker invoker = new Invoker();
    CommandParser parser =
        new CommandParser(collection, out, consoleScanner, invoker, dataFile, consoleReader);
    CommandSource source = new ConsoleSource(consoleScanner, out);

    // 3. Главный цикл обработки команд
    out.println("Application started. Type 'help' for available commands.");
    while (true) {
      try {
        String line = source.readLine();
        if (line == null) {
          break; // конец ввода (например, Ctrl+D)
        }

        Command command = parser.parse(line);
        invoker.execute(command);

        if (command instanceof ExitCommand) {
          break;
        }
      } catch (IllegalArgumentException e) {
        out.println("Error: " + e.getMessage());
      } catch (Exception e) {
        out.println("Unexpected error: " + e.getMessage());
        // можно также e.printStackTrace();
      }
    }

    out.println("Application finished.");
  }

  /**
   * Инициализирует коллекцию на основе аргументов командной строки. Логика полностью соответствует
   * исходному Main.
   *
   * @param args аргументы командной строки
   */
  private static void initializeCollection(String[] args) {
    if (args.length == 0) {
      System.err.println("No file name provided.");
      System.out.println("Initializing with an empty collection...");
      collection = new Collection();
    } else if (args.length > 1) {
      System.err.println("File name provided incorrectly.");
      System.out.println("Initializing with an empty collection...");
      collection = new Collection();
    } else if (!args[0].endsWith(".json")) {
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
                    + " doesn't unique and was skipped: "
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
      }
    }
  }
}
