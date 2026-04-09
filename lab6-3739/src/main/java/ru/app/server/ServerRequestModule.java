package ru.app.server;

import ru.app.network.Request;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;

/**
 * Server request module - deserializes incoming requests from ByteBuffer.
 *
 * <p>Converts serialized bytes back into Request objects using Java serialization.
 *
 * @author Lab6
 * @version 1.0
 */
public class ServerRequestModule {

  /**
   * Reads and deserializes a Request from the buffer.
   *
   * @param buffer byte buffer containing serialized request
   * @return Request object or null if deserialization fails
   */
  public Request read(ByteBuffer buffer) {
    try {
      byte[] data = new byte[buffer.remaining()];
      buffer.get(data);

      ByteArrayInputStream bais = new ByteArrayInputStream(data);
      ObjectInputStream ois = new ObjectInputStream(bais);

      Object obj = ois.readObject();
      ois.close();

      if (obj instanceof Request request) return request;
      return null;
    } catch (IOException | ClassNotFoundException e) {
      return null;
    }
  }
}
