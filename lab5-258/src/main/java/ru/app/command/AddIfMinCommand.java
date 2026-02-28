package ru.app.command;

import ru.app.collection.Collection;
import ru.app.io.ConsoleReader;
import ru.app.object.Product;

import java.io.PrintWriter;
import java.util.Scanner;

/** Команда добавления элемента, если его id меньше минимального. */
public class AddIfMinCommand extends AbstractCommand {
  private final Scanner scanner;
  private final ConsoleReader consoleReader;

  public AddIfMinCommand(
      Collection collection, PrintWriter out, Scanner scanner, ConsoleReader consoleReader) {
    super(collection, out);
    this.scanner = scanner;
    this.consoleReader = consoleReader;
  }

  @Override
  public String getName() {
    return "add_if_min";
  }

  @Override
  public void execute() {
    Product product = consoleReader.readProduct(scanner);
    if (product == null) {
      println("Product creation cancelled.");
      return;
    }
    try {
      collection.addIfMin(product);
    } catch (IllegalArgumentException e) {
      println("Error: " + e.getMessage());
    }
  }
}
