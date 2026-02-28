package ru.app.command;

import ru.app.collection.Collection;
import java.io.PrintWriter;

/**
 * Команда завершения программы. Служит маркером для основного цикла – после её выполнения цикл
 * должен прерваться.
 */
public class ExitCommand extends AbstractCommand {

  public ExitCommand(Collection collection, PrintWriter out) {
    super(collection, out);
  }

  @Override
  public String getName() {
    return "exit";
  }

  @Override
  public void execute() {}
}
