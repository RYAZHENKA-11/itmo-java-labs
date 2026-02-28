package ru.app.command;

import ru.app.collection.Collection;
import java.io.PrintWriter;

/** Команда удаления первого элемента коллекции. */
public class RemoveFirstCommand extends AbstractCommand {

  public RemoveFirstCommand(Collection collection, PrintWriter out) {
    super(collection, out);
  }

  @Override
  public String getName() {
    return "remove_first";
  }

  @Override
  public void execute() {
    if (collection.size() == 0) {
      println("Collection is empty.");
      return;
    }
    collection.remove();
  }
}
