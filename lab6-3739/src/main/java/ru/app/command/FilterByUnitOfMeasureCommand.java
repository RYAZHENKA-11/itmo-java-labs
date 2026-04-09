package ru.app.command;

import ru.app.collection.Collection;
import ru.app.network.Request;
import ru.app.object.UnitOfMeasure;
import ru.app.object.Product;

import java.util.List;

/**
 * Command to filter and display elements by their unit of measure.
 *
 * @author Lab6
 * @version 1.0
 */
public class FilterByUnitOfMeasureCommand extends AbstractCommand {
  @Override
  public String getName() {
    return "filter_by_unit_of_measure";
  }

  @Override
  public CommandResult execute(Request request, Collection collection) {
    String arg = request.argument();
    if (arg == null || arg.isEmpty()) return CommandResult.error("No unit of measure provided");
    try {
      UnitOfMeasure unit = UnitOfMeasure.valueOf(arg.toUpperCase());
      List<Product> filtered = collection.filterByUnitOfMeasure(unit);
      if (filtered.isEmpty()) return CommandResult.success("Filtered list is empty.");
      return CommandResult.success("Filtered products:", filtered);
    } catch (IllegalArgumentException e) {
      return CommandResult.error("Invalid unit of measure: " + arg);
    }
  }
}
