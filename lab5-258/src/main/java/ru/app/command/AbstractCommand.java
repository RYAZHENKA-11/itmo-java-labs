package ru.app.command;

import ru.app.collection.Collection;

import java.io.PrintWriter;

/** Абстрактная базовая команда, содержащая общие поля и методы. */
public abstract class AbstractCommand implements Command {
  protected final Collection collection;
  protected final PrintWriter out;

  /**
   * Конструктор.
   *
   * @param collection коллекция, с которой работает команда
   * @param out поток вывода для результатов
   */
  public AbstractCommand(Collection collection, PrintWriter out) {
    this.collection = collection;
    this.out = out;
  }

  /**
   * Возвращает имя команды (без аргументов) для истории.
   *
   * @return имя команды
   */
  public abstract String getName();

  /**
   * Удобный метод для вывода строки с переводом строки.
   *
   * @param message сообщение для вывода
   */
  protected void println(String message) {
    out.println(message);
  }
}
