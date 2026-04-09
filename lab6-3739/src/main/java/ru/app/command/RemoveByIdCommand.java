package ru.app.command;

import ru.app.collection.Collection;
import ru.app.network.Request;

/**
 * Command to remove an element from the collection by its ID.
 *
 * @author Lab6
 * @version 1.0
 */
public class RemoveByIdCommand extends AbstractCommand {
  @Override
  public String getName() {
    return "remove_by_id";
  }

  @Override
  public CommandResult execute(Request request, Collection collection) {
    Integer id = request.id();
    if (id == null) return CommandResult.error("No ID provided");
    try {
      collection.remove(id);
      return CommandResult.success("Product with id " + id + " removed");
    } catch (IllegalArgumentException e) {
      return CommandResult.error(e.getMessage());
    }
  }
}
