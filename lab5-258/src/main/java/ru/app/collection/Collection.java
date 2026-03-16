package ru.app.collection;

import ru.app.object.Product;
import ru.app.object.UnitOfMeasure;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.PriorityQueue;

/**
 * Manages a collection of {@link Product} objects stored in a priority queue (natural order by {@code id}).
 * Ensures uniqueness of identifiers and part numbers, and maintains the sum of all product prices.
 *
 * <p>The collection is created with a fixed creation date. All add, update, and remove operations
 * automatically adjust the internal unique key sets and price sum.
 */
public class Collection {
  private final PriorityQueue<Product> collection = new PriorityQueue<>();
  private final Set<Integer> ids = new HashSet<>();
  private final Set<String> partNumbers = new HashSet<>();
  private Double sumPrice = 0.0;
  private final ZonedDateTime creationDate;

  /** Creates an empty collection, setting the current date and time as the creation date. */
  public Collection() {
    creationDate = ZonedDateTime.now();
  }

  /**
   * Creates an empty collection with a given creation date.
   *
   * @param creationDate collection creation date (cannot be {@code null})
   * @throws IllegalArgumentException if {@code creationDate == null}
   */
  public Collection(ZonedDateTime creationDate) throws IllegalArgumentException {
    if (creationDate == null) throw new IllegalArgumentException("'creationDate' can't be null");
    this.creationDate = creationDate;
  }

  /**
   * Returns the current number of products in the collection.
   *
   * @return collection size
   */
  public int size() {
    return collection.size();
  }

  /**
   * Returns the collection creation date.
   *
   * @return creation date (not {@code null})
   */
  public ZonedDateTime creationDate() {
    return creationDate;
  }

  /**
   * Returns a copy of the internal product queue.
   *
   * @return new {@link PriorityQueue} with copies of all products
   */
  public PriorityQueue<Product> products() {
    return new PriorityQueue<>(collection);
  }

  /**
   * Adds a new product to the collection.
   *
   * <p>Uniqueness of {@code id} and {@code partNumber} is checked before adding. If the product
   * is successfully added, its price (if not {@code null}) is added to the total sum.
   *
   * @param product product to add (validated through {@link Product} constructor)
   * @throws IllegalArgumentException if {@code id} or {@code partNumber} are not unique
   */
  public void add(Product product) throws IllegalArgumentException {
    if (!ids.add(product.id())) throw new IllegalArgumentException("'id' must be unique.");
    if (!partNumbers.add(product.partNumber())) {
      ids.remove(product.id());
      throw new IllegalArgumentException("'partNumber' must be unique.");
    }
    collection.add(product);
    if (product.price() != null) sumPrice += product.price();
  }

  /**
   * Updates the product with the specified identifier, replacing it with new data.
   *
   * <p>Actually performs removal of the old product and addition of a new one with the same {@code id}.
   * All uniqueness constraints apply to the new product (except the {@code id} itself, which remains unchanged).
   *
   * @param id identifier of the product to update (must exist in the collection)
   * @param product new product whose fields will be assigned (except {@code id})
   * @throws IllegalArgumentException if product with specified {@code id} does not exist, or if
   *     new {@code partNumber} is not unique
   */
  public void update(Integer id, Product product) throws IllegalArgumentException {
    Product oldProduct = remove(id);
    Product newProduct =
        new Product(
            id,
            product.name(),
            product.coordinates(),
            product.price(),
            product.partNumber(),
            product.unitOfMeasure(),
            product.owner(),
            oldProduct.creationDate());
    try {
      add(newProduct);
    } catch (IllegalArgumentException e) {
      add(oldProduct);
      throw e;
    }
  }

  public boolean isExist(Integer id) {
    return collection.stream().anyMatch(x -> x.id().equals(id));
  }

  /**
   * Removes the product with the specified identifier from the collection.
   *
   * <p>After removal, the price sum is adjusted and corresponding unique keys are freed.
   *
   * @param id identifier of the product to remove
   * @throws IllegalArgumentException if product with specified {@code id} is not found
   */
  public Product remove(Integer id) throws IllegalArgumentException {
    Product product = collection.stream().filter(x -> x.id().equals(id)).findFirst().orElse(null);
    if (product == null)
      throw new IllegalArgumentException("Product with id " + id + " doesn't exist.");
    collection.remove(product);
    ids.remove(product.id());
    partNumbers.remove(product.partNumber());
    if (product.price() != null) sumPrice -= product.price();
    return product;
  }

  /**
   * Removes all products from the collection.
   *
   * <p>Clears the unique key sets and resets the price sum.
   */
  public void clear() {
    collection.clear();
    ids.clear();
    partNumbers.clear();
    sumPrice = 0.0;
  }

  /**
   * Removes the first (smallest by {@code id}) element from the queue.
   *
   * @throws java.util.NoSuchElementException if the collection is empty
   */
  public void remove() {
    Product product = collection.remove();
    ids.remove(product.id());
    partNumbers.remove(product.partNumber());
    if (product.price() != null) sumPrice -= product.price();
  }

  /**
   * Adds the product only if its {@code id} is less than the {@code id} of the first element in the
   * queue. For an empty collection, the product is always added.
   *
   * @param product the product to add
   * @return true if the product was added, false otherwise
   * @throws IllegalArgumentException if {@code id} or {@code partNumber} are not unique
   */
  public boolean addIfMin(Product product) {
    if (collection.isEmpty() || collection.element().id() > product.id()) {
      add(product);
      return true;
    }
    return false;
  }

  /**
   * Returns the sum of prices of all products in the collection.
   *
   * <p>Products with {@code null} price are not included. The sum is maintained up-to-date for all
   * collection changes.
   *
   * @return sum of prices (always not {@code null}, initial value 0.0)
   */
  public Double sumPrice() {
    return sumPrice;
  }

  /**
   * Returns the average arithmetic price of products.
   *
   * <p>Calculated as {@link #sumPrice()} / {@link #size()}. If the collection is empty, the result may
   * be {@code NaN} (0.0 / 0).
   *
   * @return average price
   */
  public Double averagePrice() {
    if (size() == 0) return 0.0;
    return sumPrice / size();
  }

  /**
   * Returns a list of products whose unit of measure matches the specified one.
   *
   * @param unitOfMeasure the unit of measure to filter by (not {@code null})
   * @return list of products (may be empty)
   */
  public List<Product> filterByUnitOfMeasure(UnitOfMeasure unitOfMeasure) {
    return collection.stream().filter(x -> x.unitOfMeasure().equals(unitOfMeasure)).toList();
  }

  /**
   * Calculates the next free identifier for a new product.
   *
   * <p>The identifier equals the maximum existing {@code id} in the collection plus one. If the
   * collection is empty, returns 1.
   *
   * @return next available identifier
   * @throws IllegalStateException if maximum identifier value is reached
   */
  public Integer nextId() {
    int maxId = collection.stream().mapToInt(Product::id).max().orElse(0);
    if (maxId >= Integer.MAX_VALUE) {
      throw new IllegalStateException("Maximum id value reached.");
    }
    return maxId + 1;
  }
}
