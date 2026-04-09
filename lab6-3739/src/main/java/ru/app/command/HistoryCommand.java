package ru.app.command;

import ru.app.collection.Collection;
import ru.app.network.Request;

/**
 * Command to display the history of executed commands.
 *
 * @author Lab6
 * @version 1.0
 */
public class HistoryCommand extends AbstractCommand {
  @Override
  public String getName() {
    return "history";
  }

  @Override
  public CommandResult execute(Request request, Collection collection) {
    return CommandResult.success("History not implemented in server mode");
  }
}
