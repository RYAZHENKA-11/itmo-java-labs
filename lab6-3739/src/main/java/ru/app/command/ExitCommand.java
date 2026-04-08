package ru.app.command;

import ru.app.collection.Collection;
import java.io.PrintWriter;

/**
 * Command to exit the program. Serves as a marker for the main loop - after its execution the loop
 * should break.
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
