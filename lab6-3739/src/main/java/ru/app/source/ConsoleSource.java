package ru.app.source;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.history.DefaultHistory;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConsoleSource implements CommandSource, AutoCloseable {
  private final LineReader reader;
  private final Path historyFile;
  private final DefaultHistory history;
  private final Terminal terminal;

  public ConsoleSource(Path historyFile) throws IOException {
    if (historyFile == null) throw new IllegalArgumentException("historyFile cannot be null");
    this.historyFile = historyFile;

    Terminal terminal;
    try {
      terminal = TerminalBuilder.builder().system(true).build();
    } catch (Exception e) {
      terminal = TerminalBuilder.builder().streams(System.in, System.out).build();
    }
    this.terminal = terminal;

    this.history = new DefaultHistory();
    this.reader = LineReaderBuilder.builder().terminal(terminal).history(history).build();
    this.history.attach(reader);
    loadHistory();

    Runtime.getRuntime().addShutdownHook(new Thread(this::saveHistory));
  }

  private void loadHistory() {
    if (Files.exists(historyFile)) {
      try {
        history.read(historyFile, false);
        history.moveToEnd();
      } catch (Exception e) {
        System.err.println("Warning: could not load history: " + e.getMessage());
      }
    }
  }

  private void saveHistory() {
    if (history != null) {
      try {
        history.write(historyFile, false);
      } catch (IOException e) {
        System.err.println("Failed to save history: " + e.getMessage());
      }
    }
  }

  @Override
  public void close() throws IOException {
    saveHistory();
    if (terminal != null) terminal.close();
  }

  @Override
  public String readLine() throws IOException {
    String line = reader.readLine("> ");
    if (line != null && !line.trim().isEmpty()) history.add(line);
    saveHistory();
    return line;
  }
}
