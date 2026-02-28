package ru.app.command;

import ru.app.collection.Collection;
import ru.app.invoker.Invoker;

import java.io.PrintWriter;
import java.util.List;

/** Команда вывода истории последних 13 команд. */
public class HistoryCommand extends AbstractCommand {
  private final Invoker invoker;

  public HistoryCommand(Collection collection, PrintWriter out, Invoker invoker) {
    super(collection, out);
    this.invoker = invoker;
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
    println("Last 13 commands:");
    for (String cmd : history) {
      println("  " + cmd);
    }
  }
}
