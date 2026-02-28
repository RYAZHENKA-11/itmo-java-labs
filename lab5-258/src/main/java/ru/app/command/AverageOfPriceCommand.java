package ru.app.command;

import ru.app.collection.Collection;
import java.io.PrintWriter;

/** Команда вывода средней цены товаров. */
public class AverageOfPriceCommand extends AbstractCommand {

  public AverageOfPriceCommand(Collection collection, PrintWriter out) {
    super(collection, out);
  }

  @Override
  public String getName() {
    return "average_of_price";
  }

  @Override
  public void execute() {
    println(String.valueOf(collection.averagePrice()));
  }
}
