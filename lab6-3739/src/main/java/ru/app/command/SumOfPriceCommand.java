package ru.app.command;

import ru.app.collection.Collection;
import ru.app.network.Request;

/**
 * Command to calculate and display the sum of prices of all elements.
 *
 * @author Lab6
 * @version 1.0
 */
public class SumOfPriceCommand extends AbstractCommand {
  @Override
  public String getName() {
    return "sum_of_price";
  }

  @Override
  public CommandResult execute(Request request, Collection collection) {
    return CommandResult.success("Sum of prices: " + collection.sumPrice());
  }
}
