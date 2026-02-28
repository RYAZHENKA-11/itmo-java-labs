package ru.app.json;

import ru.app.object.Product;
import java.io.ByteArrayOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * Utility class for reading and writing {@link PriorityQueue} of {@link Product} objects in JSON
 * format. The class provides a custom JSON parser and serializer.
 *
 * <p>The expected JSON structure is an array of product objects, each conforming to the fields
 * defined in {@link Product#toMap()} and {@link Product#fromMap(Map)}.
 *
 * <p>This class is not intended to be instantiated.
 */
public final class Json {

  private Json() {}

  /**
   * Reads a JSON array from the given input stream and converts it into a {@code
   * PriorityQueue<Product>}. The stream is read completely and then parsed.
   *
   * @param in the input stream to read from (must support mark/reset if needed, but not required)
   * @return a priority queue containing the products, sorted by their natural order (by id)
   * @throws IOException if an I/O error occurs, the JSON is malformed, or a product fails
   *     validation
   */
  public static PriorityQueue<Product> readProducts(BufferedInputStream in) throws IOException {
    String json = readAll(in);
    if (json.isBlank()) return new PriorityQueue<>();

    Object parsed = new Parser(json).parse();
    if (!(parsed instanceof List<?> list))
      throw new IOException("Root element must be a JSON array.");

    PriorityQueue<Product> queue = new PriorityQueue<>();
    for (Object item : list) {
      if (!(item instanceof Map<?, ?> map))
        throw new IOException("Each product must be a JSON object.");
      try {
        @SuppressWarnings("unchecked")
        Product product = Product.fromMap((Map<String, Object>) map);
        queue.add(product);
      } catch (IllegalArgumentException e) {
        throw new IOException("Failed to create product: " + e.getMessage(), e);
      }
    }
    return queue;
  }

  /**
   * Writes the given priority queue of products to the output writer as a JSON array. The products
   * are converted using {@link Product#toMap()}.
   *
   * @param queue the queue to write (the iteration order follows the queue's natural order)
   * @param out the writer to write the JSON to
   * @throws IOException if an I/O error occurs or a product contains unsupported data types
   */
  public static void writeProducts(PriorityQueue<Product> queue, BufferedWriter out)
      throws IOException {
    List<Map<String, Object>> list = new ArrayList<>();
    for (Product product : queue) list.add(product.toMap());

    String json = Serializer.serialize(list);
    out.write(json);
    out.flush();
  }

  /**
   * Reads all data from the input stream into a UTF-8 string.
   *
   * @param in the stream to read
   * @return the complete content as a string
   * @throws IOException if an I/O error occurs
   */
  private static String readAll(BufferedInputStream in) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    byte[] buffer = new byte[8192];
    int len;
    while ((len = in.read(buffer)) != -1) baos.write(buffer, 0, len);
    return baos.toString(StandardCharsets.UTF_8);
  }

  /**
   * A simple recursive‑descent JSON parser. It parses the input string into Java objects: {@code
   * Map<String, Object>} for objects, {@code List<Object>} for arrays, {@code String}, {@code Long}
   * for integer numbers, {@code Float} for floating‑point numbers, or {@code null}.
   */
  private static class Parser {
    private final String input;
    private int pos = 0;

    /**
     * Creates a new parser for the given JSON string.
     *
     * @param input the JSON string to parse
     */
    Parser(String input) {
      this.input = input;
    }

    /**
     * Parses the entire input as a JSON value.
     *
     * @return the parsed Java object
     * @throws IOException if the input is empty, malformed, or ends unexpectedly
     */
    Object parse() throws IOException {
      skipWhitespace();
      if (pos >= input.length()) throw new IOException("Empty JSON.");
      return parseValue();
    }

    /**
     * Parses any JSON value (object, array, string, number, null).
     *
     * @return the parsed value
     * @throws IOException on syntax error or unexpected end
     */
    private Object parseValue() throws IOException {
      skipWhitespace();
      if (pos >= input.length()) throw new IOException("Unexpected end of input.");

      char c = input.charAt(pos);
      if (c == '{') return parseObject();
      else if (c == '[') return parseArray();
      else if (c == '"') return parseString();
      else if (c == 'n') return parseNull();
      else if (c == '-' || (c >= '0' && c <= '9')) return parseNumber();
      else throw new IOException("Unexpected character: " + c + " at position " + pos);
    }

    /**
     * Parses a JSON object.
     *
     * @return a {@code Map<String, Object>} representing the object
     * @throws IOException on syntax error
     */
    private Map<String, Object> parseObject() throws IOException {
      ++pos;
      skipWhitespace();
      Map<String, Object> map = new HashMap<>();
      if (input.charAt(pos) == '}') {
        ++pos;
        return map;
      }

      while (true) {
        skipWhitespace();
        if (input.charAt(pos) != '"') throw new IOException("Expected '\"' at position " + pos);
        String key = parseString();

        skipWhitespace();
        if (input.charAt(pos) != ':') throw new IOException("Expected ':' at position " + pos);
        ++pos;
        Object value = parseValue();
        map.put(key, value);
        skipWhitespace();
        char c = input.charAt(pos);
        if (c == '}') {
          ++pos;
          break;
        } else if (c == ',') ++pos;
        else throw new IOException("Expected ',' or '}' at position " + pos);
      }
      return map;
    }

    /**
     * Parses a JSON array.
     *
     * @return a {@code List<Object>} containing the array elements
     * @throws IOException on syntax error
     */
    private List<Object> parseArray() throws IOException {
      ++pos;
      skipWhitespace();
      List<Object> list = new ArrayList<>();
      if (input.charAt(pos) == ']') {
        ++pos;
        return list;
      }

      while (true) {
        list.add(parseValue());
        skipWhitespace();
        char c = input.charAt(pos);
        if (c == ']') {
          ++pos;
          break;
        } else if (c == ',') ++pos;
        else throw new IOException("Expected ',' or ']' at position " + pos);
      }
      return list;
    }

    /**
     * Parses a JSON string (without unescaping – assumes no escaped quotes).
     *
     * @return the string content
     * @throws IOException if the string is unterminated
     */
    private String parseString() throws IOException {
      ++pos;
      StringBuilder sb = new StringBuilder();
      while (pos < input.length()) {
        char c = input.charAt(pos);
        if (c != '"') sb.append(c);
        else {
          ++pos;
          return sb.toString();
        }
        ++pos;
      }
      throw new IOException("Unterminated string.");
    }

    /**
     * Parses a JSON number and returns it as an {@code Integer} if it contains no fractional or
     * exponent part, otherwise as a {@code Float}.
     *
     * @return the parsed number
     * @throws IOException if the number format is invalid
     */
    private Number parseNumber() throws IOException {
      int start = pos;
      boolean isFloating = false;
      if (input.charAt(pos) == '-') ++pos;
      while (pos < input.length() && Character.isDigit(input.charAt(pos))) ++pos;
      if (pos < input.length() && input.charAt(pos) == '.') {
        isFloating = true;
        ++pos;
        while (pos < input.length() && Character.isDigit(input.charAt(pos))) ++pos;
      }
      if (pos < input.length() && (input.charAt(pos) == 'e' || input.charAt(pos) == 'E')) {
        isFloating = true;
        ++pos;
        if (pos < input.length() && (input.charAt(pos) == '+' || input.charAt(pos) == '-')) ++pos;
        while (pos < input.length() && Character.isDigit(input.charAt(pos))) ++pos;
      }

      String numStr = input.substring(start, pos);
      try {
        if (isFloating) return Float.parseFloat(numStr);
        else return Long.parseLong(numStr);
      } catch (NumberFormatException e) {
        throw new IOException("Invalid number format: " + numStr);
      }
    }

    /**
     * Parses the literal {@code null}.
     *
     * @return {@code null}
     * @throws IOException if the literal is not exactly "null"
     */
    private Object parseNull() throws IOException {
      if (input.startsWith("null", pos)) {
        pos += 4;
        return null;
      }
      throw new IOException("Expected 'null' at position " + pos);
    }

    private void skipWhitespace() {
      while (pos < input.length() && Character.isWhitespace(input.charAt(pos))) ++pos;
    }
  }

  /**
   * A simple JSON serializer that converts Java objects into JSON text. Supports {@code Map},
   * {@code List}, {@code String}, {@code Number}, and {@code null}.
   */
  private static class Serializer {

    /**
     * Serializes a list of maps (typically representing an array of product objects) into a JSON
     * string.
     *
     * @param list the list to serialize
     * @return the JSON string
     * @throws IOException if an unsupported type is encountered
     */
    static String serialize(List<Map<String, Object>> list) throws IOException {
      StringBuilder sb = new StringBuilder();
      serializeValue(list, sb);
      return sb.toString();
    }

    /**
     * Serializes an arbitrary Java object according to JSON rules.
     *
     * @param obj the object to serialize
     * @param sb the {@code StringBuilder} to append to
     * @throws IOException if the object type is not supported
     */
    @SuppressWarnings("unchecked")
    private static void serializeValue(Object obj, StringBuilder sb) throws IOException {
      if (obj == null) sb.append("null");
      else if (obj instanceof Map) serializeMap((Map<String, Object>) obj, sb);
      else if (obj instanceof List) serializeList((List<Map<String, Object>>) obj, sb);
      else if (obj instanceof String) serializeString((String) obj, sb);
      else if (obj instanceof Number) sb.append(obj);
      else throw new IOException("Unsupported type: " + obj.getClass());
    }

    /**
     * Serializes a {@code Map} as a JSON object.
     *
     * @param map the map to serialize
     * @param sb the {@code StringBuilder} to append to
     * @throws IOException if any value in the map is of an unsupported type
     */
    private static void serializeMap(Map<String, Object> map, StringBuilder sb) throws IOException {
      sb.append('{');
      boolean first = true;
      for (Map.Entry<String, Object> entry : map.entrySet()) {
        if (!first) sb.append(',');
        first = false;
        serializeString(entry.getKey(), sb);
        sb.append(':');
        serializeValue(entry.getValue(), sb);
      }
      sb.append('}');
    }

    /**
     * Serializes a {@code List} as a JSON array.
     *
     * @param list the list to serialize
     * @param sb the {@code StringBuilder} to append to
     * @throws IOException if any element in the list is of an unsupported type
     */
    private static void serializeList(List<Map<String, Object>> list, StringBuilder sb)
        throws IOException {
      sb.append('[');
      boolean first = true;
      for (Object item : list) {
        if (!first) sb.append(',');
        first = false;
        serializeValue(item, sb);
      }
      sb.append(']');
    }

    /**
     * Serializes a string as a JSON string literal (enclosed in double quotes).
     *
     * <p><strong>Note:</strong> This implementation does not escape special characters inside the
     * string.
     *
     * @param str the string to serialize
     * @param sb the {@code StringBuilder} to append to
     */
    private static void serializeString(String str, StringBuilder sb) {
      sb.append('"');
      sb.append(str);
      sb.append('"');
    }
  }
}
