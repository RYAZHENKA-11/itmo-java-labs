package ru.app.invoker;

import ru.app.command.Command;
import ru.app.command.AbstractCommand;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/** Инвокер, который выполняет команды и хранит историю последних 13 команд. */
public class Invoker {
  private static final int HISTORY_SIZE = 13;
  private final Queue<String> history = new LinkedList<>();

  /**
   * Выполняет команду и добавляет её имя в историю.
   *
   * @param command команда для выполнения
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
   * Возвращает копию списка истории команд.
   *
   * @return список последних имён команд
   */
  public List<String> getHistory() {
    return List.copyOf(history);
  }
}
