package ru.app.object;

import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * Товар, хранящийся в коллекции. Сравнение товаров по умолчанию осуществляется по {@link #id}
 * (естественный порядок).
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
    implements Comparable<Product> {

  /**
   * Создаёт товар с проверкой всех полей согласно требованиям ТЗ.
   *
   * @param id идентификатор (не null, >0)
   * @param name название (не null, не пустое)
   * @param coordinates координаты (не null)
   * @param price цена (может быть null; если не null, то >0 и должна быть конечным числом, не NaN и
   *     не бесконечность)
   * @param partNumber артикул (не null, длина от 22 до 83 символов)
   * @param unitOfMeasure единица измерения (не null)
   * @param owner владелец (не null)
   * @param creationDate дата создания (не null)
   * @throws IllegalArgumentException если какое-либо поле не проходит валидацию
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
    if (id == null) throw new IllegalArgumentException("'id' can't be null.");
    if (id <= 0) throw new IllegalArgumentException("'id' can't be <= 0.");
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
   * Сравнивает текущий товар с другим по {@code id}. Используется для естественной сортировки в
   * коллекциях.
   *
   * @param other другой товар
   * @return отрицательное число, ноль или положительное число, если текущий id меньше, равен или
   *     больше другого id соответственно
   */
  @Override
  public int compareTo(Product other) {
    return this.id.compareTo(other.id);
  }

  /**
   * Преобразует объект в карту для последующей сериализации. Вложенные объекты (coordinates, owner)
   * также преобразуются в карты. Дата создания сохраняется в строковом представлении
   * (ZonedDateTime.toString).
   *
   * @return карта, содержащая все поля объекта
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
   * Создаёт объект Product из карты, полученной при десериализации. Выполняет все необходимые
   * проверки через конструктор.
   *
   * @param map карта с данными
   * @return новый экземпляр Product
   * @throws IllegalArgumentException если карта null, отсутствуют обязательные ключи, значения
   *     имеют неверный тип или формат
   */
  public static Product fromMap(Map<String, Object> map) throws IllegalArgumentException {
    if (map == null) throw new IllegalArgumentException("'map' can't be null.");

    Object idObj = map.get("id");
    if (idObj == null) throw new IllegalArgumentException("Missing 'id' in map");
    if (!(idObj instanceof Number)) throw new IllegalArgumentException("'id' must be a number");
    Integer id = ((Number) idObj).intValue();

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
      price = ((Number) priceObj).floatValue();
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
