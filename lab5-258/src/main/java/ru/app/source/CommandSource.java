package ru.app.source;

import java.io.IOException;

/** Command source: reads lines from any source (console, file). */
public interface CommandSource {
  /**
   * Reads the next command line.
   *
   * @return command line or null if source is exhausted
   * @throws IOException if I/O error occurs
   */
  String readLine() throws IOException;
}
