package ru.app.command;

import ru.app.collection.Collection;
import ru.app.network.Request;
import ru.app.object.Product;

import java.time.ZonedDateTime;

/**
 * Command to add an element if its price is less than the minimum in the collection.
 *
 * @author Lab6
 * @version 1.0
 */
public class AddIfMinCommand extends AbstractCommand {
  @Override
  public String getName() {
    return "add_if_min";
  }

  @Override
  public CommandResult execute(Request request, Collection collection) {
    Product product = request.product();
    if (product == null) return CommandResult.error("No product data provided");
    Integer newId;
    try {
      newId = collection.nextId();
    } catch (Exception e) {
      newId = 1;
    }
    Product productWithId =
        new Product(
            newId,
            product.name(),
            product.coordinates(),
            product.price(),
            product.partNumber(),
            product.unitOfMeasure(),
            product.owner(),
            ZonedDateTime.now());
    boolean added = collection.addIfMin(productWithId);
    if (added) return CommandResult.success("Product was added with id=" + newId);
    return CommandResult.error("Product was not added (id is not less than minimum).");
  }
}
