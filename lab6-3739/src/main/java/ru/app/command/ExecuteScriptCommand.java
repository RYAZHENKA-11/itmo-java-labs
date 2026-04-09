package ru.app.command;

import ru.app.collection.Collection;
import ru.app.network.Request;

/**
 * Command to execute commands from a script file.
 *
 * @author Lab6
 * @version 1.0
 */
public class ExecuteScriptCommand extends AbstractCommand {
  @Override
  public String getName() {
    return "execute_script";
  }

  @Override
  public CommandResult execute(Request request, Collection collection) {
    return CommandResult.error(
        "execute_script not supported via network - use client-side execution");
  }
}
