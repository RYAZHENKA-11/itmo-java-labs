package ru.app.io;

import org.jline.reader.History;
import org.jline.reader.impl.history.DefaultHistory;
import ru.app.invoker.Invoker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class JLineHistory {
    private static final int MAX_HISTORY_SIZE = Invoker.HISTORY_SIZE;
    private final DefaultHistory history;
    private final Path historyFile;

    public JLineHistory(Path dataFilePath) {
        this.historyFile = dataFilePath.resolveSibling("command_history.txt");
        this.history = new DefaultHistory();
        load();
    }

    public void load() {
        if (!historyFile.toFile().exists()) {
            return;
        }
        try (BufferedReader reader = Files.newBufferedReader(historyFile)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (!trimmed.isEmpty()) {
                    history.add(trimmed);
                }
            }
        } catch (IOException e) {
            // Ignore loading errors, start with empty history
        }
    }

    public void save() {
        try (BufferedWriter writer = Files.newBufferedWriter(historyFile)) {
            for (History.Entry entry : history) {
                writer.write(entry.line());
                writer.newLine();
            }
        } catch (IOException e) {
            // Ignore save errors
        }
    }

    public void add(String commandName) {
        if (commandName == null || commandName.isBlank()) {
            return;
        }
        
        // Don't add duplicates
        if (history.size() > 0) {
            String lastEntry = history.get(history.size() - 1);
            if (lastEntry.equals(commandName)) {
                return;
            }
        }
        
        history.add(commandName);
    }

    public DefaultHistory getHistory() {
        return history;
    }

    public Path getHistoryFile() {
        return historyFile;
    }

    public List<String> getHistoryList() {
        List<String> result = new ArrayList<>();
        for (History.Entry entry : history) {
            result.add(entry.line());
        }
        return result;
    }

    public static String extractCommandName(String line) {
        if (line == null || line.isBlank()) return null;
        String[] parts = line.trim().split("\\s+", 2);
        return parts[0].toLowerCase();
    }
}