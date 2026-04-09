package ru.app.object;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Person (product owner). Name cannot be null or empty; location cannot be null. Birthday and
 * passport may be null.
 */
public record Person(String name, Date birthday, String passportID, Location location)
    implements Serializable {

  @Serial private static final long serialVersionUID = 1L;

  /**
   * Creates a person with validation of constraints.
   *
   * @param name name (not null, not empty)
   * @param birthday birthday (can be null)
   * @param passportID passport (can be null)
   * @param location location (not null)
   * @throws IllegalArgumentException if name is null or empty, or location is null
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
   * Converts the object to a map for serialization. Birthday is formatted in ISO 8601 with
   * milliseconds and timezone.
   *
   * @return map with keys "name", "birthday", "passportID", "location"
   */
  public Map<String, Object> toMap() {
    Map<String, Object> map = new HashMap<>();
    map.put("name", name);
    if (birthday != null) {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
      map.put("birthday", sdf.format(birthday));
    } else map.put("birthday", null);
    map.put("passportID", passportID);
    map.put("location", location.toMap());
    return map;
  }

  /**
   * Creates a Person object from a map.
   *
   * @param map map with data (keys "name", "birthday", "passportID", "location")
   * @return new Person instance
   * @throws IllegalArgumentException if map is null, required keys are missing, values have
   *     incorrect type or format
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
      if (!(bdayObj instanceof String dateStr))
        throw new IllegalArgumentException("'birthday' must be a string");
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
