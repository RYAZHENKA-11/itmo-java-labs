package ru.app.command;

import ru.app.collection.Collection;
import ru.app.io.ConsoleReader;
import ru.app.object.Product;

import java.io.PrintWriter;
import java.util.Scanner;

/** Команда обновления элемента по id. */
public class UpdateCommand extends AbstractCommand {
  private final int id;
  private final Scanner scanner;
  private final ConsoleReader consoleReader;

  public UpdateCommand(
      Collection collection,
      PrintWriter out,
      int id,
      Scanner scanner,
      ConsoleReader consoleReader) {
    super(collection, out);
    this.id = id;
    this.scanner = scanner;
    this.consoleReader = consoleReader;
  }

  @Override
  public String getName() {
    return "update";
  }

  @Override
  public void execute() {
    if (!collection.isExist(id)) {
      println("Product with id=" + id + " doesn't exist.");
      return;
    }
    Product newProduct = consoleReader.readProduct(scanner);
    if (newProduct == null) {
      println("Update cancelled.");
      return;
    }
    try {
      collection.update(id, newProduct);
      println("Product with id=" + id + " updated.");
    } catch (IllegalArgumentException e) {
      println("Error: " + e.getMessage());
    }
  }
}
