package ru.app.object;

import java.util.HashMap;
import java.util.Map;

/** Местоположение человека. Поля y и name не могут быть null; длина name не более 986. */
public record Location(float x, Integer y, long z, String name) {

  /**
   * Создаёт местоположение с проверкой ограничений.
   *
   * @param x координата X
   * @param y координата Y (не null)
   * @param z координата Z
   * @param name название места (не null, длина ≤ 986)
   * @throws IllegalArgumentException если y == null, name == null или name длиннее 986
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
   * Преобразует местоположение в карту для сериализации.
   *
   * @return карта с ключами "x", "y", "z", "name"
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
   * Создаёт объект Location из карты.
   *
   * @param map карта с данными (ключи "x", "y", "z", "name")
   * @return новый экземпляр Location
   * @throws IllegalArgumentException если карта null, отсутствуют ключи или значения неверного типа
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
    Integer y = ((Number) yObj).intValue();

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
