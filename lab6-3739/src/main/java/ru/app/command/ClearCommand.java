package ru.app.command;

import ru.app.collection.Collection;
import ru.app.network.Request;

/**
 * Command to clear all elements from the collection.
 *
 * @author Lab6
 * @version 1.0
 */
public class ClearCommand extends AbstractCommand {
  @Override
  public String getName() {
    return "clear";
  }

  @Override
  public CommandResult execute(Request request, Collection collection) {
    collection.clear();
    return CommandResult.success("Collection was cleared.");
  }
}
