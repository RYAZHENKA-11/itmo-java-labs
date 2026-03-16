package ru.app.command;

import ru.app.collection.Collection;
import ru.app.object.UnitOfMeasure;

import ru.app.object.Product;

import java.io.PrintWriter;
import java.util.List;

/** Command to output elements with the specified unit of measure. */
public class FilterByUnitOfMeasureCommand extends AbstractCommand {
  private final UnitOfMeasure unitOfMeasure;

  public FilterByUnitOfMeasureCommand(
      Collection collection, PrintWriter out, UnitOfMeasure unitOfMeasure) {
    super(collection, out);
    this.unitOfMeasure = unitOfMeasure;
  }

  @Override
  public String getName() {
    return "filter_by_unit_of_measure";
  }

  @Override
  public void execute() {
    List<Product> filtered = collection.filterByUnitOfMeasure(unitOfMeasure);
    if (filtered.isEmpty()) {
      println("Filtered list is empty.");
    } else {
      filtered.forEach(p -> println(p.toString()));
    }
  }
}
