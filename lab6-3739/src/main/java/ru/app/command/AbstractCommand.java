package ru.app.command;

import ru.app.collection.Collection;

import java.io.PrintWriter;

/** Abstract base command containing common fields and methods. */
public abstract class AbstractCommand implements Command {
  protected final Collection collection;
  protected final PrintWriter out;

  /**
   * Constructor.
   *
   * @param collection the collection the command operates on
   * @param out output stream for results
   */
  public AbstractCommand(Collection collection, PrintWriter out) {
    this.collection = collection;
    this.out = out;
  }

  /**
   * Returns the command name (without arguments) for history.
   *
   * @return command name
   */
  public abstract String getName();

  /**
   * Convenience method for printing a line.
   *
   * @param message message to print
   */
  protected void println(String message) {
    out.println(message);
  }
}
