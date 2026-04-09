package ru.app.object;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * Product stored in the collection. Product comparison by default is performed by {@link #id}
 * (natural order).
 */
public record Product(
    Integer id,
    String name,
    Coordinates coordinates,
    Float price,
    String partNumber,
    UnitOfMeasure unitOfMeasure,
    Person owner,
    ZonedDateTime creationDate)
    implements Comparable<Product>, Serializable {

  @Serial private static final long serialVersionUID = 1L;

  /**
   * Creates a product with validation of all fields according to requirements.
   *
   * @param id identifier (not null, >0)
   * @param name name (not null, not empty)
   * @param coordinates coordinates (not null)
   * @param price price (can be null; if not null, must be >0 and must be finite, not NaN and not
   *     infinity)
   * @param partNumber part number (not null, length from 22 to 83 characters)
   * @param unitOfMeasure unit of measure (not null)
   * @param owner owner (not null)
   * @param creationDate creation date (not null)
   * @throws IllegalArgumentException if any field fails validation
   */
  public Product(
      Integer id,
      String name,
      Coordinates coordinates,
      Float price,
      String partNumber,
      UnitOfMeasure unitOfMeasure,
      Person owner,
      ZonedDateTime creationDate) {
    if (id != null && id <= 0) throw new IllegalArgumentException("'id' can't be <= 0.");
    this.id = id;

    if (name == null) throw new IllegalArgumentException("'name' can't be null.");
    if (name.isEmpty()) throw new IllegalArgumentException("'name' can't be empty.");
    this.name = name;

    if (coordinates == null) throw new IllegalArgumentException("'coordinates' can't be null");
    this.coordinates = coordinates;

    if (creationDate == null) throw new IllegalArgumentException("'creationDate' can't be null");
    this.creationDate = creationDate;

    if (price != null && (price <= 0 || !Float.isFinite(price)))
      throw new IllegalArgumentException("'price' must be > 0");
    this.price = price;

    if (partNumber == null) throw new IllegalArgumentException("'partNumber' can't be null.");
    if (partNumber.length() < 22)
      throw new IllegalArgumentException("'partNumber' length must be >= 22.");
    if (partNumber.length() > 83)
      throw new IllegalArgumentException("'partNumber' length can't be > 83.");
    this.partNumber = partNumber;

    if (unitOfMeasure == null) throw new IllegalArgumentException("'unitOfMeasure' can't be null.");
    this.unitOfMeasure = unitOfMeasure;

    if (owner == null) throw new IllegalArgumentException("'owner' can't be null");
    this.owner = owner;
  }

  /**
   * Compares this product with another by {@code id}. Used for natural sorting in collections.
   *
   * @param other another product
   * @return negative, zero, or positive number if this id is less than, equal to, or greater than
   *     the other id respectively
   */
  @Override
  public int compareTo(Product other) {
    return this.id.compareTo(other.id);
  }

  /**
   * Converts the object to a map for serialization. Nested objects (coordinates, owner) are also
   * converted to maps. Creation date is saved as string representation (ZonedDateTime.toString).
   *
   * @return map containing all object fields
   */
  public Map<String, Object> toMap() {
    Map<String, Object> map = new HashMap<>();
    map.put("id", id);
    map.put("name", name);
    map.put("coordinates", coordinates.toMap());
    map.put("creationDate", creationDate.toString());
    map.put("price", price);
    map.put("partNumber", partNumber);
    map.put("unitOfMeasure", unitOfMeasure.name());
    map.put("owner", owner.toMap());
    return map;
  }

  /**
   * Creates a Product object from a map obtained during deserialization. Performs all necessary
   * validations through the constructor.
   *
   * @param map map with data
   * @return new Product instance
   * @throws IllegalArgumentException if map is null, required keys are missing, values have
   *     incorrect type or format
   */
  public static Product fromMap(Map<String, Object> map) throws IllegalArgumentException {
    if (map == null) throw new IllegalArgumentException("'map' can't be null.");

    Object idObj = map.get("id");
    if (idObj == null) throw new IllegalArgumentException("Missing 'id' in map");
    if (!(idObj instanceof Number)) throw new IllegalArgumentException("'id' must be a number");
    long idLong = ((Number) idObj).longValue();
    if (idLong <= 0 || idLong > Integer.MAX_VALUE)
      throw new IllegalArgumentException("'id' must be > 0 and <= " + Integer.MAX_VALUE);
    Integer id = (int) idLong;

    Object nameObj = map.get("name");
    if (nameObj == null) throw new IllegalArgumentException("Missing 'name' in map");
    if (!(nameObj instanceof String name))
      throw new IllegalArgumentException("'name' must be a string");

    Object coordObj = map.get("coordinates");
    if (coordObj == null) throw new IllegalArgumentException("Missing 'coordinates' in map");
    if (!(coordObj instanceof Map<?, ?>))
      throw new IllegalArgumentException("'coordinates' must be a Map");
    @SuppressWarnings("unchecked")
    Coordinates coordinates = Coordinates.fromMap((Map<String, Object>) coordObj);

    Float price = null;
    Object priceObj = map.get("price");
    if (priceObj != null) {
      if (!(priceObj instanceof Number))
        throw new IllegalArgumentException("'price' must be a number");
      float priceVal = ((Number) priceObj).floatValue();
      if (priceVal <= 0 || !Float.isFinite(priceVal))
        throw new IllegalArgumentException("'price' must be > 0 and finite");
      price = priceVal;
    }

    Object pnObj = map.get("partNumber");
    if (pnObj == null) throw new IllegalArgumentException("Missing 'partNumber' in map");
    if (!(pnObj instanceof String partNumber))
      throw new IllegalArgumentException("'partNumber' must be a string");

    Object uomObj = map.get("unitOfMeasure");
    if (uomObj == null) throw new IllegalArgumentException("Missing 'unitOfMeasure' in map");
    if (!(uomObj instanceof String))
      throw new IllegalArgumentException("'unitOfMeasure' must be a string");
    UnitOfMeasure unitOfMeasure;
    try {
      unitOfMeasure = UnitOfMeasure.valueOf((String) uomObj);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Invalid unitOfMeasure value: " + uomObj);
    }

    Object ownerObj = map.get("owner");
    if (ownerObj == null) throw new IllegalArgumentException("Missing 'owner' in map");
    if (!(ownerObj instanceof Map<?, ?>))
      throw new IllegalArgumentException("'owner' must be a Map");
    @SuppressWarnings("unchecked")
    Person owner = Person.fromMap((Map<String, Object>) ownerObj);

    Object dateObj = map.get("creationDate");
    if (dateObj == null) throw new IllegalArgumentException("Missing 'creationDate' in map");
    if (!(dateObj instanceof String dateStr))
      throw new IllegalArgumentException("'creationDate' must be a string");
    ZonedDateTime creationDate;
    try {
      creationDate = ZonedDateTime.parse(dateStr);
    } catch (DateTimeParseException e) {
      throw new IllegalArgumentException("Invalid creationDate format: " + dateStr);
    }

    return new Product(
        id, name, coordinates, price, partNumber, unitOfMeasure, owner, creationDate);
  }
}
