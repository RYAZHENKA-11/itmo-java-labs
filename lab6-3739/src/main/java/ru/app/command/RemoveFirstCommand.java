package ru.app.command;

import ru.app.collection.Collection;
import ru.app.network.Request;

/**
 * Command to remove the first element from the collection.
 *
 * @author Lab6
 * @version 1.0
 */
public class RemoveFirstCommand extends AbstractCommand {
  @Override
  public String getName() {
    return "remove_first";
  }

  @Override
  public CommandResult execute(Request request, Collection collection) {
    if (collection.size() == 0) return CommandResult.error("Collection is empty.");
    collection.remove();
    return CommandResult.success("First product removed");
  }
}
