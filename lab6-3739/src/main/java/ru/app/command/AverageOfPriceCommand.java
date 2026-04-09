package ru.app.command;

import ru.app.collection.Collection;
import ru.app.network.Request;

/**
 * Command to calculate and display the average price of all elements.
 *
 * @author Lab6
 * @version 1.0
 */
public class AverageOfPriceCommand extends AbstractCommand {
  @Override
  public String getName() {
    return "average_of_price";
  }

  @Override
  public CommandResult execute(Request request, Collection collection) {
    return CommandResult.success(String.valueOf(collection.averagePrice()));
  }
}
