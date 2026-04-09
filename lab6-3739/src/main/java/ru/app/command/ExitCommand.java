package ru.app.command;

import ru.app.collection.Collection;
import ru.app.network.Request;

/**
 * Command to exit the application.
 *
 * @author Lab6
 * @version 1.0
 */
public class ExitCommand extends AbstractCommand {
  @Override
  public String getName() {
    return "exit";
  }

  @Override
  public CommandResult execute(Request request, Collection collection) {
    return CommandResult.error("exit command is not available via network");
  }
}
