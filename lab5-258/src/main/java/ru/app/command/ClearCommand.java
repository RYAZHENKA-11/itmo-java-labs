package ru.app.command;

import ru.app.collection.Collection;
import java.io.PrintWriter;

/** Команда очистки коллекции. */
public class ClearCommand extends AbstractCommand {

  public ClearCommand(Collection collection, PrintWriter out) {
    super(collection, out);
  }

  @Override
  public String getName() {
    return "clear";
  }

  @Override
  public void execute() {
    collection.clear();
    println("Collection was cleared.");
  }
}
