package ru.app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.app.collection.Collection;
import ru.app.json.Json;
import ru.app.object.Product;
import ru.app.server.ServerConnectionModule;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.PriorityQueue;

public class Server {
  private static final Logger logger = LogManager.getLogger(Server.class);
  private static final int PORT = 8888;
  private static final int BUFFER_SIZE = 65535;

  public static void main(String[] args) {
    logger.info("Server starting on port {}", PORT);
    String filePath = extractFilePath(args);
    Collection collection = initializeCollection(filePath);
    logger.info("Server started on port {}", PORT);
    logger.info("Collection loaded: {} elements", collection.size());

    ServerConnectionModule server = null;
    try {
      server = new ServerConnectionModule(PORT, collection, BUFFER_SIZE, filePath);
      startServerConsole(server);
      server.start();
    } catch (IOException e) {
      logger.error("Server error: {}", e.getMessage(), e);
      if (server != null) server.saveCollection();
    }
  }

  private static String extractFilePath(String[] args) {
    if (args == null || args.length == 0) return null;
    String fileName = args[0];
    if (!fileName.endsWith(".json")) return null;
    return fileName;
  }

  private static void startServerConsole(ServerConnectionModule server) {
    Thread consoleThread =
        new Thread(
            () -> {
              BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
              System.out.println("Server console available. Type 'save' to save, 'exit' to stop.");
              while (true) {
                try {
                  String line = console.readLine();
                  if (line == null) break;
                  line = line.trim();
                  if (line.equals("save")) server.saveCollection();
                  else if (line.equals("exit")) {
                    logger.info("Shutting down server...");
                    server.saveCollection();
                    System.exit(0);
                  }
                } catch (IOException e) {
                  break;
                }
              }
            });
    consoleThread.setDaemon(true);
    consoleThread.start();
  }

  private static Collection initializeCollection(String filePath) {
    if (filePath == null) {
      logger.info("No file provided. Starting with empty collection.");
      return new Collection();
    }

    File dataFile = new File(filePath);

    if (!dataFile.exists()) {
      logger.warn("File not found: {}. Starting with empty collection.", filePath);
      return new Collection();
    }

    try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(dataFile))) {
      PriorityQueue<Product> products = Json.readProducts(bis);
      Collection collection = new Collection();
      for (Product p : products) {
        try {
          collection.add(p);
        } catch (IllegalArgumentException e) {
          logger.warn("Skipped product id={}: {}", p.id(), e.getMessage());
        }
      }
      return collection;
    } catch (IOException e) {
      logger.error("Read error: {}. Starting with empty collection.", e.getMessage());
      return new Collection();
    }
  }
}
