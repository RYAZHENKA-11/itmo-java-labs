package ru.app.object;

import java.util.HashMap;
import java.util.Map;

/** Person location. Fields y and name cannot be null; name length must not exceed 986. */
public record Location(float x, Integer y, long z, String name) {

  /**
   * Creates a location with validation of constraints.
   *
   * @param x coordinate X
   * @param y coordinate Y (not null)
   * @param z coordinate Z
   * @param name place name (not null, length shorter than 986)
   * @throws IllegalArgumentException if y is null, name is null, or name length exceeds 986
   */
  public Location(float x, Integer y, long z, String name) {
    if (y == null) throw new IllegalArgumentException("'y' can't be null.");
    this.y = y;

    if (name == null) throw new IllegalArgumentException("'name' can't be null.");
    if (name.length() > 986) throw new IllegalArgumentException("'name' length can't be > 986.");
    this.name = name;

    this.x = x;
    this.z = z;
  }

  /**
   * Converts the location to a map for serialization.
   *
   * @return map with keys "x", "y", "z", "name"
   */
  public Map<String, Object> toMap() {
    Map<String, Object> map = new HashMap<>();
    map.put("x", x);
    map.put("y", y);
    map.put("z", z);
    map.put("name", name);
    return map;
  }

  /**
   * Creates a Location object from a map.
   *
   * @param map map with data (keys "x", "y", "z", "name")
   * @return new Location instance
   * @throws IllegalArgumentException if map is null, keys are missing, or values have incorrect type
   */
  public static Location fromMap(Map<String, Object> map) throws IllegalArgumentException {
    if (map == null) throw new IllegalArgumentException("'map' can't be null.");

    Object xObj = map.get("x");
    if (xObj == null) throw new IllegalArgumentException("Missing 'x' in map");
    if (!(xObj instanceof Number)) throw new IllegalArgumentException("'x' must be a number");
    float x = ((Number) xObj).floatValue();

    Object yObj = map.get("y");
    if (yObj == null) throw new IllegalArgumentException("Missing 'y' in map");
    if (!(yObj instanceof Number)) throw new IllegalArgumentException("'y' must be a number");
    long yLong = ((Number) yObj).longValue();
    if (yLong < Integer.MIN_VALUE || yLong > Integer.MAX_VALUE) {
      throw new IllegalArgumentException("'y' must be within Integer range");
    }
    Integer y = (int) yLong;

    Object zObj = map.get("z");
    if (zObj == null) throw new IllegalArgumentException("Missing 'z' in map");
    if (!(zObj instanceof Number)) throw new IllegalArgumentException("'z' must be a number");
    long z = ((Number) zObj).longValue();

    Object nameObj = map.get("name");
    if (nameObj == null) throw new IllegalArgumentException("Missing 'name' in map");
    if (!(nameObj instanceof String name))
      throw new IllegalArgumentException("'name' must be a string");

    return new Location(x, y, z, name);
  }
}
