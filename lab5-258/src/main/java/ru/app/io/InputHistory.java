package ru.app.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class InputHistory {
    private static final int MAX_HISTORY_SIZE = 13;
    private final List<String> history = new ArrayList<>();
    private int currentIndex = -1;
    private String currentInput = "";
    private final Path historyFile;

    public InputHistory(Path basePath) {
        this.historyFile = basePath.resolveSibling("command_history.txt");
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
            for (String cmd : history) {
                writer.write(cmd);
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
        if (!history.isEmpty() && history.get(history.size() - 1).equals(commandName)) {
            return;
        }
        history.add(commandName);
        while (history.size() > MAX_HISTORY_SIZE) {
            history.remove(0);
        }
    }

    public String getPrevious() {
        if (history.isEmpty()) {
            return currentInput;
        }
        if (currentIndex == -1) {
            currentIndex = history.size() - 1;
        } else if (currentIndex > 0) {
            currentIndex--;
        }
        return history.get(currentIndex);
    }

    public String getNext() {
        if (history.isEmpty() || currentIndex == -1) {
            return currentInput;
        }
        if (currentIndex < history.size() - 1) {
            currentIndex++;
            return history.get(currentIndex);
        } else {
            currentIndex = -1;
            return currentInput;
        }
    }

    public void setCurrentInput(String input) {
        this.currentInput = input;
    }

    public boolean hasHistory() {
        return !history.isEmpty();
    }

    public void clearNavigation() {
        currentIndex = -1;
        currentInput = "";
    }
}