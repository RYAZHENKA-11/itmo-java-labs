package ru.app.command;

import ru.app.collection.Collection;
import ru.app.invoker.Invoker;
import ru.app.io.ConsoleReader;
import ru.app.parser.CommandParser;
import ru.app.source.FileSource;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/** Команда выполнения скрипта из файла. */
public class ExecuteScriptCommand extends AbstractCommand {
  private final String fileName;
  private final Invoker invoker;
  private final File dataFile; // глобальный файл для save
  private final ConsoleReader consoleReader;

  public ExecuteScriptCommand(
      Collection collection,
      PrintWriter out,
      String fileName,
      Invoker invoker,
      File dataFile,
      ConsoleReader consoleReader) {
    super(collection, out);
    this.fileName = fileName;
    this.invoker = invoker;
    this.dataFile = dataFile;
    this.consoleReader = consoleReader;
  }

  @Override
  public String getName() {
    return "execute_script";
  }

  @Override
  public void execute() {
    Path path = Paths.get(fileName);
    if (!path.toFile().exists() || !path.toFile().canRead()) {
      println("File doesn't exist or unreadable.");
      return;
    }

    try (FileSource fileSource = new FileSource(path);
        Scanner fileScanner = new Scanner(path)) {

      CommandParser fileParser =
          new CommandParser(collection, out, fileScanner, invoker, dataFile, consoleReader);

      String line;
      while ((line = fileSource.readLine()) != null) {
        line = line.trim();
        if (line.isEmpty()) continue;
        out.println(">>> " + line);
        try {
          Command command = fileParser.parse(line);
          invoker.execute(command);
          if (command instanceof ExitCommand) {
            println("Exit command ignored in script.");
            break;
          }
        } catch (IllegalArgumentException e) {
          out.println("Error: " + e.getMessage());
        }
      }
    } catch (IOException e) {
      out.println("Error: " + e.getMessage());
    }
  }
}
