package ru.app.command;

import ru.app.collection.Collection;
import ru.app.network.Request;
import ru.app.object.Product;

import java.time.ZonedDateTime;

/**
 * Command to add a new product to the collection.
 *
 * @author Lab6
 * @version 1.0
 */
public class AddCommand extends AbstractCommand {

  @Override
  public String getName() {
    return "add";
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
    try {
      collection.add(productWithId);
      return CommandResult.success("Product added with id=" + newId);
    } catch (IllegalArgumentException e) {
      return CommandResult.error(e.getMessage());
    }
  }
}
