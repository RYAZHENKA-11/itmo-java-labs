package ru.app.object;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Человек (владелец товара). Имя не может быть null или пустым; местоположение не может быть null.
 * Дата рождения и паспорт могут быть null.
 */
public record Person(String name, Date birthday, String passportID, Location location) {

  /**
   * Создаёт человека с проверкой ограничений.
   *
   * @param name имя (не null, не пустое)
   * @param birthday дата рождения (может быть null)
   * @param passportID паспорт (может быть null)
   * @param location местоположение (не null)
   * @throws IllegalArgumentException если name == null или пусто, или location == null
   */
  public Person(String name, Date birthday, String passportID, Location location) {
    if (name == null) throw new IllegalArgumentException("'name' can't be null.");
    if (name.isEmpty()) throw new IllegalArgumentException("'name' can't be empty.");
    this.name = name;

    this.birthday = birthday;

    this.passportID = passportID;

    if (location == null) throw new IllegalArgumentException("'location' can't be null.");
    this.location = location;
  }

  /**
   * Преобразует объект в карту для сериализации. Дата рождения форматируется в ISO 8601 с
   * миллисекундами и часовым поясом.
   *
   * @return карта с ключами "name", "birthday", "passportID", "location"
   */
  public Map<String, Object> toMap() {
    Map<String, Object> map = new HashMap<>();
    map.put("name", name);
    if (birthday != null) {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
      map.put("birthday", sdf.format(birthday));
    } else {
      map.put("birthday", null);
    }
    map.put("passportID", passportID);
    map.put("location", location.toMap());
    return map;
  }

  /**
   * Создаёт объект Person из карты.
   *
   * @param map карта с данными (ключи "name", "birthday", "passportID", "location")
   * @return новый экземпляр Person
   * @throws IllegalArgumentException если карта null, отсутствуют обязательные ключи, значения
   *     имеют неверный тип или формат
   */
  public static Person fromMap(Map<String, Object> map) throws IllegalArgumentException {
    if (map == null) throw new IllegalArgumentException("'map' can't be null.");

    Object nameObj = map.get("name");
    if (nameObj == null) throw new IllegalArgumentException("Missing 'name' in map");
    if (!(nameObj instanceof String name))
      throw new IllegalArgumentException("'name' must be a string");

    Date birthday = null;
    Object bdayObj = map.get("birthday");
    if (bdayObj != null) {
      if (!(bdayObj instanceof String))
        throw new IllegalArgumentException("'birthday' must be a string");
      String dateStr = (String) map.get("birthday");
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
      try {
        birthday = sdf.parse(dateStr);
      } catch (ParseException e) {
        throw new IllegalArgumentException("Invalid birthday format: " + dateStr);
      }
    }

    String passportID = null;
    Object pidObj = map.get("passportID");
    if (pidObj != null) {
      if (!(pidObj instanceof String))
        throw new IllegalArgumentException("'passportID' must be a string");
      passportID = (String) pidObj;
    }

    Object locObj = map.get("location");
    if (locObj == null) throw new IllegalArgumentException("Missing 'location' in map");
    if (!(locObj instanceof Map<?, ?>))
      throw new IllegalArgumentException("'location' must be a Map");
    @SuppressWarnings("unchecked")
    Location location = Location.fromMap((Map<String, Object>) locObj);

    return new Person(name, birthday, passportID, location);
  }
}
