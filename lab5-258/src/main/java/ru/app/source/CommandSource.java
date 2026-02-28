package ru.app.source;

import java.io.IOException;

/** Источник команд: читает строки из какого-либо источника (консоль, файл). */
public interface CommandSource {
  /**
   * Читает следующую строку команды.
   *
   * @return строка команды или null, если источник исчерпан
   * @throws IOException если ошибка ввода/вывода
   */
  String readLine() throws IOException;
}
