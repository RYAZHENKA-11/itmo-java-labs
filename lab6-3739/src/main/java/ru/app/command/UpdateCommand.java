package ru.app.command;

import ru.app.collection.Collection;
import ru.app.network.Request;
import ru.app.object.Product;

/**
 * Command to update an existing element in the collection by its ID.
 *
 * @author Lab6
 * @version 1.0
 */
public class UpdateCommand extends AbstractCommand {
  @Override
  public String getName() {
    return "update";
  }

  @Override
  public CommandResult execute(Request request, Collection collection) {
    Integer id = request.id();
    Product product = request.product();
    if (id == null) return CommandResult.error("No ID provided");
    if (product == null) return CommandResult.error("No product data provided");
    if (!collection.isExist(id))
      return CommandResult.error("Product with id=" + id + " doesn't exist.");
    try {
      collection.update(id, product);
      return CommandResult.success("Product with id=" + id + " updated.");
    } catch (IllegalArgumentException e) {
      return CommandResult.error(e.getMessage());
    }
  }
}
