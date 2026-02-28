package ru.app.command;

import ru.app.collection.Collection;
import java.io.PrintWriter;

/** Команда удаления элемента по его id. */
public class RemoveByIdCommand extends AbstractCommand {
  private final int id;

  public RemoveByIdCommand(Collection collection, PrintWriter out, int id) {
    super(collection, out);
    this.id = id;
  }

  @Override
  public String getName() {
    return "remove_by_id";
  }

  @Override
  public void execute() {
    try {
      collection.remove(id);
    } catch (IllegalArgumentException e) {
      println("Error: " + e.getMessage());
    }
  }
}
