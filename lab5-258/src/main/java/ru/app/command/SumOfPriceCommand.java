package ru.app.command;

import ru.app.collection.Collection;
import java.io.PrintWriter;

/** Команда вывода суммы цен всех товаров. */
public class SumOfPriceCommand extends AbstractCommand {

  public SumOfPriceCommand(Collection collection, PrintWriter out) {
    super(collection, out);
  }

  @Override
  public String getName() {
    return "sum_of_price";
  }

  @Override
  public void execute() {
    println(String.valueOf(collection.sumPrice()));
  }
}
