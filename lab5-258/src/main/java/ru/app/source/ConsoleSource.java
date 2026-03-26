package ru.app.source;

import ru.app.io.InputHistory;

import java.io.IOException;
import java.io.PrintWriter;

public class ConsoleSource implements CommandSource {
  private final PrintWriter out;
  private final InputHistory history;

  public ConsoleSource(PrintWriter out, InputHistory history) {
    this.out = out;
    this.history = history;
  }

  @Override
  public String readLine() throws IOException {
    out.print("> ");
    out.flush();
    
    StringBuilder buffer = new StringBuilder();
    history.clearNavigation();
    
    while (true) {
      int ch = System.in.read();
      
      if (ch == -1) {
        return null;
      }
      
      if (ch == '\n' || ch == '\r') {
        out.println();
        break;
      }
      
      if (ch == 27) { // ESC
        int next1 = System.in.read();
        if (next1 == -1) continue;
        int next2 = System.in.read();
        if (next2 == -1) continue;
        
        if (next1 == '[' && next2 == 'A') { // Up arrow
          String prev = history.getPrevious();
          clearLine(buffer.length());
          buffer.setLength(0);
          buffer.append(prev);
          out.print(prev);
          out.flush();
          history.setCurrentInput("");
        } else if (next1 == '[' && next2 == 'B') { // Down arrow
          String next = history.getNext();
          clearLine(buffer.length());
          buffer.setLength(0);
          buffer.append(next);
          out.print(next);
          out.flush();
          history.setCurrentInput("");
        }
        continue;
      }
      
      if (ch == 127 || ch == 8) { // Delete or Backspace
        if (buffer.length() > 0) {
          buffer.deleteCharAt(buffer.length() - 1);
          out.print("\b \b");
          out.flush();
        }
        continue;
      }
      
      if (ch >= 32) {
        buffer.append((char) ch);
        out.print((char) ch);
        out.flush();
      }
    }
    
    String result = buffer.toString().trim();
    history.setCurrentInput(result);
    return result.isEmpty() ? null : result;
  }
  
  private void clearLine(int length) {
    for (int i = 0; i < length; i++) {
      out.print("\b \b");
    }
    out.flush();
  }
}