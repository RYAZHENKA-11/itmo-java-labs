package ru.app.command;

import ru.app.collection.Collection;
import ru.app.network.Request;

/**
 * Command to display information about the collection.
 *
 * @author Lab6
 * @version 1.0
 */
public class InfoCommand extends AbstractCommand {
  @Override
  public String getName() {
    return "info";
  }

  @Override
  public CommandResult execute(Request request, Collection collection) {
    String info =
        "Type: java.util.PriorityQueue\n"
            + "Init date: "
            + collection.creationDate()
            + "\n"
            + "Count of elements: "
            + collection.size();
    return CommandResult.success(info);
  }
}
