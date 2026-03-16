package ru.app.source;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

/** Command source from file. */
public class FileSource implements CommandSource, AutoCloseable {
  private final Scanner scanner;
  private boolean closed = false;

  public FileSource(Path path) throws IOException {
    this.scanner = new Scanner(path);
  }

  @Override
  public String readLine() throws IOException {
    if (closed) return null;
    if (scanner.hasNextLine()) {
      return scanner.nextLine();
    } else {
      return null;
    }
  }

  @Override
  public void close() {
    if (!closed) {
      closed = true;
      scanner.close();
    }
  }
}
