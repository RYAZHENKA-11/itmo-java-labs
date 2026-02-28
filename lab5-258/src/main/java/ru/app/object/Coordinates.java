package ru.app.object;

import java.util.HashMap;
import java.util.Map;

/** Координаты товара. Оба поля не могут быть null. */
public record Coordinates(Integer x, Float y) {

  /**
   * Создаёт координаты с проверкой на null.
   *
   * @param x координата X (не null)
   * @param y координата Y (не null)
   * @throws IllegalArgumentException если x или y равны null
   */
  public Coordinates(Integer x, Float y) {
    if (x == null) throw new IllegalArgumentException("'x' can't be null.");
    this.x = x;

    if (y == null) throw new IllegalArgumentException("'y' can't be null.");
    this.y = y;
  }

  /**
   * Преобразует координаты в карту для сериализации.
   *
   * @return карта с ключами "x" и "y"
   */
  public Map<String, Object> toMap() {
    Map<String, Object> map = new HashMap<>();
    map.put("x", x);
    map.put("y", y);
    return map;
  }

  /**
   * Создаёт объект Coordinates из карты.
   *
   * @param map карта с данными (должна содержать ключи "x" и "y")
   * @return новый экземпляр Coordinates
   * @throws IllegalArgumentException если карта null, отсутствуют ключи или значения имеют неверный
   *     тип
   */
  public static Coordinates fromMap(Map<String, Object> map) throws IllegalArgumentException {
    if (map == null) throw new IllegalArgumentException("'map' can't be null.");

    Object xObj = map.get("x");
    if (xObj == null) throw new IllegalArgumentException("Missing 'x' in map");
    if (!(xObj instanceof Number)) throw new IllegalArgumentException("'x' must be a number");
    Integer x = ((Number) xObj).intValue();

    Object yObj = map.get("y");
    if (yObj == null) throw new IllegalArgumentException("Missing 'y' in map");
    if (!(yObj instanceof Number)) throw new IllegalArgumentException("'y' must be a number");
    Float y = ((Number) yObj).floatValue();

    return new Coordinates(x, y);
  }
}
