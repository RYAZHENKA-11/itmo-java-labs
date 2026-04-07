package ru.app.parser;

import ru.app.collection.Collection;
import ru.app.command.*;
import ru.app.invoker.Invoker;
import ru.app.io.ConsoleReader;
import ru.app.object.UnitOfMeasure;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

/** Command line parser. Converts a string to a command object. */
public class CommandParser {
  private final Collection collection;
  private final PrintWriter out;
  private final Scanner scanner;
  private final Invoker invoker;
  private final File dataFile;
  private final ConsoleReader consoleReader;

  private final Command helpCommand;
  private final Command infoCommand;
  private final Command showCommand;
  private final Command clearCommand;
  private final Command saveCommand;
  private final Command exitCommand;
  private final Command removeFirstCommand;
  private final Command historyCommand;
  private final Command sumOfPriceCommand;
  private final Command averageOfPriceCommand;

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

    helpCommand = new HelpCommand(collection, out);
    infoCommand = new InfoCommand(collection, out);
    showCommand = new ShowCommand(collection, out);
    clearCommand = new ClearCommand(collection, out);
    saveCommand = new SaveCommand(collection, out, dataFile);
    exitCommand = new ExitCommand(collection, out);
    removeFirstCommand = new RemoveFirstCommand(collection, out);
    historyCommand = new HistoryCommand(collection, out, invoker);
    sumOfPriceCommand = new SumOfPriceCommand(collection, out);
    averageOfPriceCommand = new AverageOfPriceCommand(collection, out);
  }

  /**
   * Parses a string and returns the corresponding command.
   *
   * @param line command string (may contain arguments)
   * @return command object
   * @throws IllegalArgumentException if command is unknown or arguments are incorrect
   */
  public Command parse(String line) {
    if (line == null || line.isBlank())
      throw new IllegalArgumentException("Command cannot be empty.");
    String[] parts = line.trim().split("\\s+", 2);
    String commandName = parts[0].toLowerCase();
    String argument = parts.length > 1 ? parts[1] : null;

    return switch (commandName) {
      case "help" -> helpCommand;
      case "info" -> infoCommand;
      case "show" -> showCommand;
      case "add" -> new AddCommand(collection, out, scanner, consoleReader);
      case "update" -> {
        if (argument == null) throw new IllegalArgumentException("update requires id argument");
        int updateId = parseInt(argument);
        yield new UpdateCommand(collection, out, updateId, scanner, consoleReader);
      }
      case "remove_by_id" -> {
        if (argument == null)
          throw new IllegalArgumentException("remove_by_id requires id argument");
        int removeId = parseInt(argument);
        yield new RemoveByIdCommand(collection, out, removeId);
      }
      case "clear" -> clearCommand;
      case "save" -> saveCommand;
      case "execute_script" -> {
        if (argument == null)
          throw new IllegalArgumentException("execute_script requires file name");
        yield new ExecuteScriptCommand(collection, out, argument, invoker, dataFile, consoleReader);
      }
      case "exit" -> exitCommand;
      case "remove_first" -> removeFirstCommand;
      case "add_if_min" -> new AddIfMinCommand(collection, out, scanner, consoleReader);
      case "history" -> historyCommand;
      case "sum_of_price" -> sumOfPriceCommand;
      case "average_of_price" -> averageOfPriceCommand;
      case "filter_by_unit_of_measure" -> {
        if (argument == null)
          throw new IllegalArgumentException("filter_by_unit_of_measure requires unit");
        UnitOfMeasure uom = parseUnitOfMeasure(argument);
        yield new FilterByUnitOfMeasureCommand(collection, out, uom);
      }
      default -> throw new IllegalArgumentException("Unknown command: " + commandName);
    };
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
