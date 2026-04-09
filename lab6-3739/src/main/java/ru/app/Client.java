package ru.app;

import ru.app.client.ClientConnectionModule;
import ru.app.io.ConsoleReader;
import ru.app.network.CommandType;
import ru.app.network.Request;
import ru.app.network.Response;
import ru.app.object.Product;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Client application entry point. Connects to server via UDP and allows user to send commands.
 *
 * <p>Usage: java -jar client.jar
 *
 * @author Lab6
 * @version 1.0
 */
public class Client {
  private static final String HOST = "localhost";
  private static final int PORT = 8888;
  private static final int TIMEOUT_MS = 3000;
  private static final int MAX_RETRIES = 3;

  /**
   * Main entry point for the client application.
   *
   * @param args command line arguments (not used)
   */
  public static void main(String[] args) {
    PrintWriter out = new PrintWriter(System.out, true);
    Scanner scanner = new Scanner(System.in);
    ConsoleReader consoleReader = new ConsoleReader(out, null);

    out.println("Client started. Connecting to " + HOST + ":" + PORT);
    out.println("Type 'help' for available commands, 'exit' to quit.");

    ClientConnectionModule client;
    try {
      client = new ClientConnectionModule(HOST, PORT, TIMEOUT_MS, MAX_RETRIES);
    } catch (Exception e) {
      out.println("Failed to connect: " + e.getMessage());
      return;
    }

    label:
    while (true) {
      try {
        out.print("> ");
        out.flush();
        String line = scanner.nextLine().trim();
        switch (line) {
          case "":
            continue;
          case "exit":
            out.println("Client shutting down.");
            break label;
          case "help":
            printHelp(out);
            continue;
        }

        Request request = parseCommand(line, scanner, consoleReader, out);
        if (request == null) {
          out.println("Invalid command. Type 'help' for available commands.");
          continue;
        }

        if (request.type() == CommandType.EXECUTE_SCRIPT)
          executeScript(request.argument(), client, scanner, consoleReader, out);
        else {
          Response response = client.sendRequest(request);
          printResponse(out, response);
        }

      } catch (Exception e) {
        out.println("Error: " + e.getMessage());
      }
    }

    client.close();
  }

  /**
   * Prints available commands to console.
   *
   * @param out output stream
   */
  private static void printHelp(PrintWriter out) {
    out.println("Available commands:");
    out.println("  help                           - Show this help");
    out.println("  add                            - Add new product");
    out.println("  show                           - Show all products (sorted by name)");
    out.println("  info                           - Show collection info");
    out.println("  clear                          - Clear collection");
    out.println("  remove_by_id <id>              - Remove product by ID");
    out.println("  update <id>                    - Update product by ID");
    out.println("  remove_first                   - Remove first product");
    out.println("  add_if_min                     - Add product if ID is minimal");
    out.println("  sum_of_price                   - Sum of all prices");
    out.println("  average_of_price               - Average price");
    out.println("  filter_by_unit_of_measure <u>  - Filter by unit of measure");
    out.println("  history                        - Show command history");
    out.println("  execute_script <file>          - Execute commands from file");
    out.println("  exit                           - Exit client");
  }

  /**
   * Prints server response to console.
   *
   * @param out output stream
   * @param response server response
   */
  private static void printResponse(PrintWriter out, Response response) {
    if (response == null) {
      out.println("No response from server.");
      return;
    }
    if (!response.success()) {
      out.println("Error: " + response.message());
      return;
    }
    if (response.data() != null) {
      out.println(response.message());
      response.data().forEach(out::println);
    } else out.println(response.message());
  }

  /**
   * Parses user input into Request object.
   *
   * @param line command line
   * @param scanner scanner for reading input
   * @param consoleReader product reader
   * @param out output stream
   * @return Request object or null if invalid
   */
  private static Request parseCommand(
      String line, Scanner scanner, ConsoleReader consoleReader, PrintWriter out) {
    String[] parts = line.split("\\s+", 2);
    String cmd = parts[0].toLowerCase();
    String arg = parts.length > 1 ? parts[1] : null;

    return switch (cmd) {
      case "add" -> {
        Product product = consoleReader.readProduct(scanner);
        if (product == null) {
          out.println("Product creation cancelled.");
          yield null;
        }
        yield new Request(CommandType.ADD, product);
      }
      case "show" -> new Request(CommandType.SHOW);
      case "info" -> new Request(CommandType.INFO);
      case "clear" -> new Request(CommandType.CLEAR);
      case "remove_by_id" -> {
        if (arg == null) {
          out.println("Usage: remove_by_id <id>");
          yield null;
        }
        yield parseIdRequest(CommandType.REMOVE_BY_ID, arg, out);
      }
      case "update" -> {
        if (arg == null) {
          out.println("Usage: update <id>");
          yield null;
        }
        yield parseIdRequest(CommandType.UPDATE, arg, out);
      }
      case "remove_first" -> new Request(CommandType.REMOVE_FIRST);
      case "add_if_min" -> {
        Product product = consoleReader.readProduct(scanner);
        if (product == null) {
          out.println("Product creation cancelled.");
          yield null;
        }
        yield new Request(CommandType.ADD_IF_MIN, product);
      }
      case "sum_of_price" -> new Request(CommandType.SUM_OF_PRICE);
      case "average_of_price" -> new Request(CommandType.AVERAGE_OF_PRICE);
      case "filter_by_unit_of_measure" -> {
        if (arg == null) {
          out.println("Usage: filter_by_unit_of_measure <unit>");
          yield null;
        }
        yield new Request(CommandType.FILTER_BY_UNIT_OF_MEASURE, null, null, arg);
      }
      case "history" -> new Request(CommandType.HISTORY);
      case "execute_script" -> {
        if (arg == null) {
          out.println("Usage: execute_script <filename>");
          yield null;
        }
        yield new Request(CommandType.EXECUTE_SCRIPT, null, null, arg);
      }
      default -> {
        out.println("Unknown command: " + cmd + ". Type 'help' for available commands.");
        yield null;
      }
    };
  }

  /**
   * Parses ID argument.
   *
   * @param type command type
   * @param arg ID as string
   * @param out output stream
   * @return Request or null if invalid
   */
  private static Request parseIdRequest(CommandType type, String arg, PrintWriter out) {
    try {
      int id = Integer.parseInt(arg);
      return new Request(type, null, id, null);
    } catch (NumberFormatException e) {
      out.println("Invalid ID: " + arg);
      return null;
    }
  }

  /**
   * Executes commands from script file.
   *
   * @param filePath path to script file
   * @param client connection module
   * @param scanner scanner for input
   * @param consoleReader product reader
   * @param out output stream
   */
  private static void executeScript(
      String filePath,
      ClientConnectionModule client,
      Scanner scanner,
      ConsoleReader consoleReader,
      PrintWriter out) {
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      String line;
      int lineNum = 0;
      while ((line = br.readLine()) != null) {
        lineNum++;
        line = line.trim();
        if (line.isEmpty() || line.startsWith("#")) continue;

        out.println("Executing: " + line);
        Request request = parseCommand(line, scanner, consoleReader, out);
        if (request == null) {
          out.println("Error at line " + lineNum + ": invalid command");
          continue;
        }

        Response response = client.sendRequest(request);
        printResponse(out, response);
        out.println();
      }
      out.println("Script execution completed.");
    } catch (Exception e) {
      out.println("Script execution error: " + e.getMessage());
    }
  }
}
