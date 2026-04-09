package ru.app.client;

import ru.app.network.Request;
import ru.app.network.Response;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

/**
 * Client connection module - handles UDP communication with server.
 *
 * <p>Sends requests to server and receives responses with retry logic for handling temporary server
 * unavailability.
 *
 * @author Lab6
 * @version 1.0
 */
public class ClientConnectionModule {
  private final InetSocketAddress serverAddress;
  private final int maxRetries;
  private final DatagramSocket socket;

  /**
   * Creates a new client connection module.
   *
   * @param host server hostname
   * @param port server port
   * @param timeoutMs timeout in milliseconds for each retry
   * @param maxRetries maximum number of retry attempts
   * @throws Exception if socket cannot be created
   */
  public ClientConnectionModule(String host, int port, int timeoutMs, int maxRetries)
      throws Exception {
    this.serverAddress = new InetSocketAddress(host, port);
    this.maxRetries = maxRetries;
    this.socket = new DatagramSocket();
    this.socket.setSoTimeout(timeoutMs);
  }

  /**
   * Sends request to server with retry logic.
   *
   * @param request request to send
   * @return server response or error response
   */
  public Response sendRequest(Request request) {
    for (int attempt = 1; attempt <= maxRetries; attempt++) {
      try {
        byte[] requestData = serializeRequest(request);
        DatagramPacket requestPacket =
            new DatagramPacket(requestData, requestData.length, serverAddress);
        socket.send(requestPacket);

        byte[] responseBuffer = new byte[65535];
        DatagramPacket responsePacket = new DatagramPacket(responseBuffer, responseBuffer.length);
        socket.receive(responsePacket);

        return deserializeResponse(responseBuffer, responsePacket.getLength());

      } catch (IOException e) {
        if (attempt == maxRetries) return Response.error("Server unavailable: " + e.getMessage());
      }
    }
    return Response.error("Server unavailable after " + maxRetries + " attempts");
  }

  /**
   * Serializes request to byte array.
   *
   * @param request request to serialize
   * @return serialized bytes
   * @throws IOException if serialization fails
   */
  private byte[] serializeRequest(Request request) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(baos);
    oos.writeObject(request);
    oos.close();
    return baos.toByteArray();
  }

  /**
   * Deserializes response from byte array.
   *
   * @param data serialized data
   * @param length data length
   * @return deserialized response
   * @throws IOException if deserialization fails
   */
  private Response deserializeResponse(byte[] data, int length) throws IOException {
    ByteArrayInputStream bais = new ByteArrayInputStream(data, 0, length);
    ObjectInputStream ois = new ObjectInputStream(bais);
    try {
      Object obj = ois.readObject();
      if (obj instanceof Response response) return response;
      return Response.error("Invalid response type");
    } catch (ClassNotFoundException e) {
      return Response.error("Invalid response format");
    }
  }

  /** Closes the client socket. */
  public void close() {
    if (socket != null && !socket.isClosed()) socket.close();
  }
}
