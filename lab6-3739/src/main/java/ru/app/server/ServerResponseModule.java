package ru.app.server;

import ru.app.network.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

/**
 * Server response module - serializes and sends responses to clients.
 *
 * <p>Converts Response objects to bytes using Java serialization and sends them via UDP datagram.
 *
 * @author Lab6
 * @version 1.0
 */
public class ServerResponseModule {
  private final DatagramChannel channel;

  /**
   * Creates a new response module.
   *
   * @param channel datagram channel for sending responses
   */
  public ServerResponseModule(DatagramChannel channel) {
    this.channel = channel;
  }

  /**
   * Sends response to client.
   *
   * @param response response to send
   * @param clientAddress client address to send to
   * @throws IOException if send fails
   */
  public void send(Response response, InetSocketAddress clientAddress) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(baos);
    oos.writeObject(response);
    oos.close();

    byte[] data = baos.toByteArray();
    ByteBuffer buffer = ByteBuffer.wrap(data);
    channel.send(buffer, clientAddress);
  }
}
