package ru.app.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.app.collection.Collection;
import ru.app.network.Request;
import ru.app.network.Response;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;

/**
 * Server connection module - handles UDP client connections using non-blocking I/O. Acts as the
 * main server loop that accepts requests, processes them, and sends responses.
 *
 * <p>This module integrates:
 *
 * <ul>
 *   <li>{@link ServerRequestModule} - deserializes incoming requests
 *   <li>{@link ServerCommandModule} - executes commands on collection
 *   <li>{@link ServerResponseModule} - serializes and sends responses
 * </ul>
 *
 * @author Lab6
 * @version 1.0
 */
public class ServerConnectionModule {
  private static final Logger logger = LogManager.getLogger(ServerConnectionModule.class);
  private final DatagramChannel channel;
  private final Selector selector;
  private final int bufferSize;
  private final String dataFilePath;

  private final ServerRequestModule requestReader;
  private final ServerCommandModule commandExecutor;
  private final ServerResponseModule responseSender;

  /**
   * Creates a new server connection module.
   *
   * @param port UDP port to listen on
   * @param collection collection to manage
   * @param bufferSize buffer size for receiving data
   * @param dataFilePath path to data file for saving collection
   * @throws IOException if server cannot be initialized
   */
  public ServerConnectionModule(
      int port, Collection collection, int bufferSize, String dataFilePath) throws IOException {
    this.bufferSize = bufferSize;
    this.dataFilePath = dataFilePath;

    this.channel = DatagramChannel.open();
    channel.bind(new InetSocketAddress(port));
    channel.configureBlocking(false);

    this.selector = Selector.open();
    channel.register(selector, SelectionKey.OP_READ);

    this.requestReader = new ServerRequestModule();
    this.commandExecutor = new ServerCommandModule(collection);
    this.responseSender = new ServerResponseModule(channel);
  }

  /**
   * Starts the server event loop. Runs in non-blocking mode using Selector.
   *
   * @throws IOException if server socket fails
   */
  public void start() throws IOException {
    logger.info("Server listening on port {}", channel.socket().getLocalPort());

    while (true) {
      selector.select();

      Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
      while (keys.hasNext()) {
        SelectionKey key = keys.next();
        keys.remove();
        if (key.isReadable()) processRequest();
      }
    }
  }

  /** Processes a single request from a client. */
  private void processRequest() {
    ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
    InetSocketAddress clientAddress = null;

    try {
      clientAddress = (InetSocketAddress) channel.receive(buffer);

      if (clientAddress != null) {
        buffer.flip();

        Request request = requestReader.read(buffer);
        if (request == null) {
          logger.warn("Invalid request format from {}", clientAddress);
          responseSender.send(Response.error("Invalid request format"), clientAddress);
          return;
        }

        logger.info("Received {} request from {}", request.type(), clientAddress);
        Response response = commandExecutor.execute(request);
        responseSender.send(response, clientAddress);
        logger.info("Sent {} response to {}", response.success() ? "success" : "error", clientAddress);
      }
    } catch (IOException e) {
      logger.error("Error processing request from {}: {}", clientAddress, e.getMessage(), e);
      if (clientAddress != null) {
        try {
          responseSender.send(Response.error("Server error: " + e.getMessage()), clientAddress);
        } catch (IOException ex) {
          logger.error("Failed to send error response to {}: {}", clientAddress, ex.getMessage(), ex);
        }
      }
    }
  }

  /** Saves the collection to file. */
  public void saveCollection() {
    commandExecutor.saveCollection(dataFilePath);
  }
}
