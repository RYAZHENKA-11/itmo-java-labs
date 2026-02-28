package ru.app.source;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

/** Источник команд из файла. */
public class FileSource implements CommandSource, AutoCloseable {
  private final Scanner scanner;

  public FileSource(Path path) throws IOException {
    this.scanner = new Scanner(path);
  }

  @Override
  public String readLine() throws IOException {
    if (scanner.hasNextLine()) {
      return scanner.nextLine();
    } else {
      scanner.close();
      return null;
    }
  }

  @Override
  public void close() {
    scanner.close();
  }
}
