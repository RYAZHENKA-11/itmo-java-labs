package ru.app.parser;

import ru.app.collection.Collection;
import ru.app.command.*;
import ru.app.invoker.Invoker;
import ru.app.io.ConsoleReader;
import ru.app.object.UnitOfMeasure;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

/** Парсер командной строки. Преобразует строку в объект команды. */
public class CommandParser {
  private final Collection collection;
  private final PrintWriter out;
  private final Scanner scanner; // сканер для интерактивных команд (add, update, add_if_min)
  private final Invoker invoker;
  private final File dataFile; // файл для сохранения (может быть null)
  private final ConsoleReader consoleReader;

  public CommandParser(
      Collection collection,
      PrintWriter out,
      Scanner scanner,
      Invoker invoker,
      File dataFile,
      ConsoleReader consoleReader) {
    this.collection = collection;
    this.out = out;
    this.scanner = scanner;
    this.invoker = invoker;
    this.dataFile = dataFile;
    this.consoleReader = consoleReader;
  }

  /**
   * Разбирает строку и возвращает соответствующую команду.
   *
   * @param line строка команды (может содержать аргументы)
   * @return объект команды
   * @throws IllegalArgumentException если команда неизвестна или аргументы некорректны
   */
  public Command parse(String line) {
    String[] parts = line.split("\\s+", 2);
    String commandName = parts[0].toLowerCase();
    String argument = parts.length > 1 ? parts[1] : null;

    switch (commandName) {
      case "help":
        return new HelpCommand(collection, out);
      case "info":
        return new InfoCommand(collection, out);
      case "show":
        return new ShowCommand(collection, out);
      case "add":
        return new AddCommand(collection, out, scanner, consoleReader);
      case "update":
        if (argument == null) throw new IllegalArgumentException("update requires id argument");
        int updateId = parseInt(argument);
        return new UpdateCommand(collection, out, updateId, scanner, consoleReader);
      case "remove_by_id":
        if (argument == null)
          throw new IllegalArgumentException("remove_by_id requires id argument");
        int removeId = parseInt(argument);
        return new RemoveByIdCommand(collection, out, removeId);
      case "clear":
        return new ClearCommand(collection, out);
      case "save":
        return new SaveCommand(collection, out, dataFile);
      case "execute_script":
        if (argument == null)
          throw new IllegalArgumentException("execute_script requires file name");
        return new ExecuteScriptCommand(
            collection, out, argument, invoker, dataFile, consoleReader);
      case "exit":
        return new ExitCommand(collection, out);
      case "remove_first":
        return new RemoveFirstCommand(collection, out);
      case "add_if_min":
        return new AddIfMinCommand(collection, out, scanner, consoleReader);
      case "history":
        return new HistoryCommand(collection, out, invoker);
      case "sum_of_price":
        return new SumOfPriceCommand(collection, out);
      case "average_of_price":
        return new AverageOfPriceCommand(collection, out);
      case "filter_by_unit_of_measure":
        if (argument == null)
          throw new IllegalArgumentException("filter_by_unit_of_measure requires unit");
        UnitOfMeasure uom = parseUnitOfMeasure(argument);
        return new FilterByUnitOfMeasureCommand(collection, out, uom);
      default:
        throw new IllegalArgumentException("Unknown command: " + commandName);
    }
  }

  private int parseInt(String s) {
    try {
      return Integer.parseInt(s);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Invalid integer format: " + s);
    }
  }

  private UnitOfMeasure parseUnitOfMeasure(String s) {
    try {
      return UnitOfMeasure.valueOf(s.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(
          "Invalid unit of measure. Available: "
              + java.util.Arrays.toString(UnitOfMeasure.values()));
    }
  }
}
