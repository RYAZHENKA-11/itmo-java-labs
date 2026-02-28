package ru.app.command;

import ru.app.collection.Collection;
import java.io.PrintWriter;

/** Команда вывода информации о коллекции. */
public class InfoCommand extends AbstractCommand {

  public InfoCommand(Collection collection, PrintWriter out) {
    super(collection, out);
  }

  @Override
  public String getName() {
    return "info";
  }

  @Override
  public void execute() {
    println("Type: java.util.PriorityQueue");
    println("Init date: " + collection.creationDate());
    println("Count of elements: " + collection.size());
  }
}
