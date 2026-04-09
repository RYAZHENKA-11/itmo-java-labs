package ru.app.object;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/** Product coordinates. Both fields cannot be null. */
public record Coordinates(Integer x, Float y) implements Serializable {

  @Serial private static final long serialVersionUID = 1L;

  /**
   * Creates coordinates with null validation.
   *
   * @param x coordinate X (not null)
   * @param y coordinate Y (not null)
   * @throws IllegalArgumentException if x or y are null
   */
  public Coordinates(Integer x, Float y) {
    if (x == null) throw new IllegalArgumentException("'x' can't be null.");
    this.x = x;

    if (y == null) throw new IllegalArgumentException("'y' can't be null.");
    this.y = y;
  }

  /**
   * Converts coordinates to a map for serialization.
   *
   * @return map with keys "x" and "y"
   */
  public Map<String, Object> toMap() {
    Map<String, Object> map = new HashMap<>();
    map.put("x", x);
    map.put("y", y);
    return map;
  }

  /**
   * Creates a Coordinates object from a map.
   *
   * @param map map with data (must contain keys "x" and "y")
   * @return new Coordinates instance
   * @throws IllegalArgumentException if map is null, keys are missing, or values have incorrect
   *     type
   */
  public static Coordinates fromMap(Map<String, Object> map) throws IllegalArgumentException {
    if (map == null) throw new IllegalArgumentException("'map' can't be null.");

    Object xObj = map.get("x");
    if (xObj == null) throw new IllegalArgumentException("Missing 'x' in map");
    if (!(xObj instanceof Number)) throw new IllegalArgumentException("'x' must be a number");
    long xLong = ((Number) xObj).longValue();
    if (xLong < Integer.MIN_VALUE || xLong > Integer.MAX_VALUE)
      throw new IllegalArgumentException("'x' must be within Integer range");
    Integer x = (int) xLong;

    Object yObj = map.get("y");
    if (yObj == null) throw new IllegalArgumentException("Missing 'y' in map");
    if (!(yObj instanceof Number)) throw new IllegalArgumentException("'y' must be a number");
    Float y = ((Number) yObj).floatValue();

    return new Coordinates(x, y);
  }
}
