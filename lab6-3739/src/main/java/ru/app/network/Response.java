package ru.app.network;

import java.io.Serializable;
import java.util.List;

/**
 * Response object sent from server to client.
 *
 * <p>Contains success status, message, and optional data. Serialized for UDP transmission.
 *
 * @author Lab6
 * @version 1.0
 */
public record Response(boolean success, String message, List<?> data) implements Serializable {

  /**
   * Creates an error response.
   *
   * @param message error message
   * @return error response
   */
  public static Response error(String message) {
    return new Response(false, message, null);
  }

  /**
   * Creates a successful response.
   *
   * @param message success message
   * @return success response
   */
  public static Response success(String message) {
    return new Response(true, message, null);
  }

  /**
   * Creates a successful response with data.
   *
   * @param message success message
   * @param data response data
   * @return success response with data
   */
  public static Response successData(String message, List<?> data) {
    return new Response(true, message, data);
  }
}
