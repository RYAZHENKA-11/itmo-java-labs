package ru.app.invoker;

import ru.app.command.Command;
import ru.app.command.AbstractCommand;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/** Invoker that executes commands and stores history of last 13 commands. */
public class Invoker {
  public static final int HISTORY_SIZE = 13;
  private final Queue<String> history = new LinkedList<>();

  /**
   * Executes a command and adds its name to history.
   *
   * @param command command to execute
   */
  public void execute(Command command) {
    if (command instanceof AbstractCommand) {
      String name = ((AbstractCommand) command).getName();
      history.add(name);
      if (history.size() > HISTORY_SIZE) history.poll();
    }
    command.execute();
  }

  /**
   * Returns a copy of the command history list.
   *
   * @return list of last command names
   */
  public List<String> getHistory() {
    return List.copyOf(history);
  }

  public void setHistory(List<String> historyList) {
    history.clear();
    for (String cmd : historyList) {
      history.add(cmd);
      if (history.size() > HISTORY_SIZE) history.poll();
    }
  }
}
