package ru.app.source;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/** Источник команд из консоли. */
public class ConsoleSource implements CommandSource {
  private final Scanner scanner;
  private final PrintWriter out;

  /**
   * Конструктор.
   *
   * @param scanner сканер для чтения из System.in
   * @param out поток для вывода приглашения
   */
  public ConsoleSource(Scanner scanner, PrintWriter out) {
    this.scanner = scanner;
    this.out = out;
  }

  @Override
  public String readLine() throws IOException {
    out.print("> ");
    out.flush();
    if (scanner.hasNextLine()) return scanner.nextLine();
    else return null;
  }
}
