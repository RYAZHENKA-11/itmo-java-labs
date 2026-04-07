package ru.app.source;

import java.io.PrintWriter;
import java.util.Scanner;

/** Command source from console. */
public class ConsoleSource implements CommandSource {
  private final Scanner scanner;
  private final PrintWriter out;

  /**
   * Constructor.
   *
   * @param scanner scanner for reading from System.in
   * @param out stream for printing prompt
   */
  public ConsoleSource(Scanner scanner, PrintWriter out) {
    this.scanner = scanner;
    this.out = out;
  }

  @Override
  public String readLine() {
    out.print("> ");
    out.flush();
    if (scanner.hasNextLine()) return scanner.nextLine();
    else return null;
  }
}
