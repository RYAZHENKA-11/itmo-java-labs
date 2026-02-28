package ru.app.command;

import ru.app.collection.Collection;
import ru.app.json.Json;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;

/** Команда сохранения коллекции в файл. */
public class SaveCommand extends AbstractCommand {
  private final File file;

  public SaveCommand(Collection collection, PrintWriter out, File file) {
    super(collection, out);
    this.file = file;
  }

  @Override
  public String getName() {
    return "save";
  }

  @Override
  public void execute() {
    if (file == null) {
      println("No file specified. Restart with a file.");
      return;
    }
    try (BufferedWriter writer = Files.newBufferedWriter(file.toPath())) {
      Json.writeProducts(collection.products(), writer);
    } catch (IOException e) {
      println("Error: " + e.getMessage());
    }
  }
}
