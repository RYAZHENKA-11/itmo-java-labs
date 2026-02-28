package ru.app.collection;

import ru.app.object.Product;
import ru.app.object.UnitOfMeasure;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.PriorityQueue;

/**
 * Управляет коллекцией товаров {@link Product}, хранящихся в очереди с приоритетом (естественный
 * порядок по {@code id}). Гарантирует уникальность идентификаторов и артикулов, а также
 * поддерживает актуальное значение суммы цен всех товаров.
 *
 * <p>Коллекция создаётся с фиксированной датой создания. Все операции добавления, обновления и
 * удаления автоматически корректируют внутренние множества уникальных ключей и сумму цен.
 */
public class Collection {
  private final PriorityQueue<Product> collection = new PriorityQueue<>();
  private final Set<Integer> ids = new HashSet<>();
  private final Set<String> partNumbers = new HashSet<>();
  private Double sumPrice = 0.0;
  private final ZonedDateTime creationDate;

  /** Создаёт пустую коллекцию, устанавливая текущую дату и время как дату создания. */
  public Collection() {
    creationDate = ZonedDateTime.now();
  }

  /**
   * Создаёт пустую коллекцию с заданной датой создания.
   *
   * @param creationDate дата создания коллекции (не может быть {@code null})
   * @throws IllegalArgumentException если {@code creationDate == null}
   */
  public Collection(ZonedDateTime creationDate) throws IllegalArgumentException {
    if (creationDate == null) throw new IllegalArgumentException("'creationDate' can't be null");
    this.creationDate = creationDate;
  }

  /**
   * Возвращает текущее количество товаров в коллекции.
   *
   * @return размер коллекции
   */
  public int size() {
    return collection.size();
  }

  /**
   * Возвращает дату создания коллекции.
   *
   * @return дата создания (не {@code null})
   */
  public ZonedDateTime creationDate() {
    return creationDate;
  }

  /**
   * Возвращает копию внутренней очереди товаров.
   *
   * @return новая очередь {@link PriorityQueue} с копиями всех товаров
   */
  public PriorityQueue<Product> products() {
    return new PriorityQueue<>(collection);
  }

  /**
   * Добавляет новый товар в коллекцию.
   *
   * <p>Перед добавлением проверяется уникальность {@code id} и {@code partNumber}. Если товар
   * успешно добавлен, его цена (если не {@code null}) прибавляется к общей сумме.
   *
   * @param product добавляемый товар (проходит валидацию конструктора {@link Product})
   * @throws IllegalArgumentException если {@code id} или {@code partNumber} не уникальны
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
   * Обновляет товар с указанным идентификатором, заменяя его новыми данными.
   *
   * <p>Фактически выполняет удаление старого товара и добавление нового с тем же {@code id}. Все
   * ограничения уникальности применяются к новому товару (кроме самого {@code id}, который остаётся
   * прежним).
   *
   * @param id идентификатор обновляемого товара (должен существовать в коллекции)
   * @param product новый товар, чьи поля будут присвоены (кроме {@code id})
   * @throws IllegalArgumentException если товар с указанным {@code id} не существует, или если
   *     новый {@code partNumber} не уникален
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
   * Удаляет товар с заданным идентификатором из коллекции.
   *
   * <p>После удаления корректируется сумма цен и освобождаются соответствующие уникальные ключи.
   *
   * @param id идентификатор удаляемого товара
   * @throws IllegalArgumentException если товар с таким {@code id} не найден
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
   * Удаляет все товары из коллекции.
   *
   * <p>Сбрасывает множества уникальных ключей и обнуляет сумму цен.
   */
  public void clear() {
    collection.clear();
    ids.clear();
    partNumbers.clear();
    sumPrice = 0.0;
  }

  /**
   * Удаляет первый (наименьший по {@code id}) товар из очереди.
   *
   * <p>Эквивалентно вызову {@link PriorityQueue#remove()} для внутренней очереди.
   *
   * @throws java.util.NoSuchElementException если коллекция пуста
   */
  public void remove() {
    if (collection.isEmpty()) return;
    Product product = collection.remove();
    ids.remove(product.id());
    partNumbers.remove(product.partNumber());
    if (product.price() != null) sumPrice -= product.price();
  }

  /**
   * Добавляет товар, только если его {@code id} меньше, чем {@code id} первого элемента очереди.
   *
   * <p><strong>Важно:</strong> метод предполагает, что коллекция не пуста. В противном случае вызов
   * {@link PriorityQueue#element()} приведёт к {@link java.util.NoSuchElementException}.
   *
   * @param product добавляемый товар
   * @throws IllegalArgumentException если {@code id} или {@code partNumber} не уникальны
   * @throws java.util.NoSuchElementException если коллекция пуста
   */
  public void addIfMin(Product product) {
    if (collection.isEmpty() || collection.element().id() > product.id()) add(product);
  }

  /**
   * Возвращает сумму цен всех товаров в коллекции.
   *
   * <p>Товары с ценой {@code null} не учитываются. Сумма поддерживается актуально при всех
   * изменениях коллекции.
   *
   * @return сумма цен (всегда не {@code null}, начальное значение 0.0)
   */
  public Double sumPrice() {
    return sumPrice;
  }

  /**
   * Возвращает среднюю арифметическую цену товаров.
   *
   * <p>Вычисляется как {@link #sumPrice()} / {@link #size()}. Если коллекция пуста, результат может
   * быть {@code NaN} (0.0 / 0).
   *
   * @return средняя цена
   */
  public Double averagePrice() {
    if (size() == 0) return 0.0;
    return sumPrice / size();
  }

  /**
   * Возвращает список товаров, у которых единица измерения совпадает с заданной.
   *
   * @param unitOfMeasure искомая единица измерения (не {@code null})
   * @return список товаров (может быть пустым)
   */
  public List<Product> filterByUnitOfMeasure(UnitOfMeasure unitOfMeasure) {
    return collection.stream().filter(x -> x.unitOfMeasure().equals(unitOfMeasure)).toList();
  }

  /**
   * Вычисляет следующий свободный идентификатор для нового товара.
   *
   * <p>Идентификатор равен максимальному существующему {@code id} в коллекции плюс один. Если
   * коллекция пуста, возвращает 1.
   *
   * @return следующий доступный идентификатор
   */
  public Integer nextId() {
    return collection.stream().mapToInt(Product::id).max().orElse(0) + 1;
  }
}
