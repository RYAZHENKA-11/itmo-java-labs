package ru.app.command;

import ru.app.collection.Collection;
import ru.app.network.Request;

/**
 * Command to save the collection to a file.
 *
 * @author Lab6
 * @version 1.0
 */
public class SaveCommand extends AbstractCommand {
  @Override
  public String getName() {
    return "save";
  }

  @Override
  public CommandResult execute(Request request, Collection collection) {
    return CommandResult.error("save command is server-only, use server console");
  }
}
