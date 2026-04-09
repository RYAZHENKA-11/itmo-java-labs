package ru.app.command;

import ru.app.collection.Collection;
import ru.app.network.Request;

/**
 * Command interface for executing operations on collection.
 *
 * <p>Each command implements this interface and handles its own logic for processing a Request and
 * returning CommandResult.
 *
 * @author Lab6
 * @version 1.0
 */
public interface Command {

  /**
   * Executes the command.
   *
   * @param request client request containing command type and data
   * @param collection collection to operate on
   * @return command execution result
   */
  CommandResult execute(Request request, Collection collection);
}
