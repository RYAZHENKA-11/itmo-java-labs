package ru.app.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.app.collection.Collection;
import ru.app.command.AbstractCommand;
import ru.app.command.CommandResult;
import ru.app.network.Request;
import ru.app.network.Response;
import ru.app.json.Json;
import ru.app.object.Product;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.PriorityQueue;

/**
 * Server command module - processes commands and manages collection.
 *
 * <p>Uses Command pattern to execute commands on the collection. Converts CommandResult to Response
 * for sending back to client.
 *
 * @author Lab6
 * @version 1.0
 */
public class ServerCommandModule {
  private static final Logger logger = LogManager.getLogger(ServerCommandModule.class);
  private final Collection collection;
  private final CommandFactory commandFactory;

  /**
   * Creates a new command module.
   *
   * @param collection collection to manage
   */
  public ServerCommandModule(Collection collection) {
    this.collection = collection;
    this.commandFactory = new CommandFactory();
  }

  /**
   * Executes a request and returns response.
   *
   * @param request client request
   * @return response to send back to client
   */
  public Response execute(Request request) {
    if (request == null || request.type() == null) return Response.error("Invalid request");
    AbstractCommand command = commandFactory.create(request.type());
    CommandResult result = command.execute(request, collection);
    return convertToResponse(result);
  }

  /**
   * Converts CommandResult to Response.
   *
   * @param result command execution result
   * @return response object
   */
  private Response convertToResponse(CommandResult result) {
    if (result == null) return Response.error("No result");
    if (result.success()) {
      if (result.data() != null) return Response.successData(result.message(), result.data());
      return Response.success(result.message());
    }
    return Response.error(result.message());
  }

  /**
   * Saves collection to JSON file.
   *
   * @param filePath path to save file
   */
  public void saveCollection(String filePath) {
    if (filePath == null) {
      logger.warn("No file path specified for save");
      return;
    }
    try {
      PriorityQueue<Product> products = collection.products();
      try (BufferedWriter writer =
          new BufferedWriter(
              new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8))) {
        Json.writeProducts(products, writer);
      }
      logger.info("Collection saved to {}", filePath);
    } catch (Exception e) {
      logger.error("Failed to save collection to {}: {}", filePath, e.getMessage(), e);
    }
  }
}
