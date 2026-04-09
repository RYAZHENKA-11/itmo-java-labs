package ru.app.network;

import ru.app.object.Product;

import java.io.Serializable;
import java.util.Objects;

/**
 * Request object sent from client to server.
 *
 * <p>Contains command type and associated data for execution. Serialized for UDP transmission.
 *
 * @author Lab6
 * @version 1.0
 */
public record Request(CommandType type, Object data, Integer id, String argument)
    implements Serializable {

  /**
   * Creates a request with type and product data.
   *
   * @param type command type
   * @param data product data
   */
  public Request(CommandType type, Object data) {
    this(type, data, null, null);
  }

  /**
   * Creates a request with only type.
   *
   * @param type command type
   */
  public Request(CommandType type) {
    this(type, null, null, null);
  }

  /**
   * Extracts product from data field.
   *
   * @return product or null if data is not Product
   */
  public Product product() {
    return data instanceof Product p ? p : null;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Request request = (Request) o;
    return type == request.type
        && Objects.equals(data, request.data)
        && Objects.equals(id, request.id)
        && Objects.equals(argument, request.argument);
  }
}
