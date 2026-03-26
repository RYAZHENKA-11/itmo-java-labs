package ru.app.command;

import ru.app.collection.Collection;
import ru.app.invoker.Invoker;
import ru.app.io.JLineHistory;

import java.io.PrintWriter;
import java.util.List;

/** Command to output history of last 13 commands. */
public class HistoryCommand extends AbstractCommand {
  private final Invoker invoker;
  private JLineHistory jLineHistory;

  public HistoryCommand(Collection collection, PrintWriter out, Invoker invoker) {
    super(collection, out);
    this.invoker = invoker;
  }

  public void setJLineHistory(JLineHistory jLineHistory) {
    this.jLineHistory = jLineHistory;
  }

  @Override
  public String getName() {
    return "history";
  }

  @Override
  public void execute() {
    List<String> history = invoker.getHistory();
    if (history.isEmpty()) {
      println("History is empty.");
      return;
    }
    println("Last " + Invoker.HISTORY_SIZE + " commands:");
    for (String cmd : history) {
      println("  " + cmd);
    }
  }
}
