package ru.app.command;

import ru.app.collection.Collection;
import ru.app.object.Product;

import java.io.PrintWriter;

/** Command to output all elements of the collection. */
public class ShowCommand extends AbstractCommand {

  public ShowCommand(Collection collection, PrintWriter out) {
    super(collection, out);
  }

  @Override
  public String getName() {
    return "show";
  }

  @Override
  public void execute() {
    if (collection.size() == 0) {
      println("Empty.");
      return;
    }
    collection.products().stream().sorted().forEach(p -> println(p.toString()));
  }
}
