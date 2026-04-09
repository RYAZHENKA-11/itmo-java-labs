package ru.app.command;

import ru.app.collection.Collection;
import ru.app.network.Request;
import ru.app.object.Product;

import java.util.Comparator;
import java.util.List;

/**
 * Command to display all products in the collection sorted by name.
 *
 * @author Lab6
 * @version 1.0
 */
public class ShowCommand extends AbstractCommand {

  @Override
  public String getName() {
    return "show";
  }

  @Override
  public CommandResult execute(Request request, Collection collection) {
    if (collection.size() == 0) return CommandResult.success("Empty.");
    List<Product> products =
        collection.products().stream().sorted(Comparator.comparing(Product::name)).toList();
    return CommandResult.success("Products:", products);
  }
}
