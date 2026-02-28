package ru.app.io;

import ru.app.collection.Collection;
import ru.app.object.*;

import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

/**
 * Утилитарный класс для чтения данных продукта с валидацией. Не хранит состояние, все методы
 * получают Scanner как параметр.
 */
public class ConsoleReader {
  private final PrintWriter out;
  private final Collection collection;

  public ConsoleReader(PrintWriter out, Collection collection) {
    this.out = out;
    this.collection = collection;
  }

  public Product readProduct(Scanner scanner) {
    String name = readString(scanner, "Enter name (not empty):", false, 1, Integer.MAX_VALUE);
    if (name == null) return null;
    Coordinates coordinates = readCoordinates(scanner);
    if (coordinates == null) return null;
    Float price = readFloat(scanner, "Enter price (empty or >0):", true, 0.0f);
    String partNumber = readString(scanner, "Enter part number (22-83 symbols):", false, 22, 83);
    if (partNumber == null) return null;
    UnitOfMeasure uom = readUnitOfMeasure(scanner);
    if (uom == null) return null;
    Person owner = readPerson(scanner);
    if (owner == null) return null;
    return new Product(
        collection.nextId(), name, coordinates, price, partNumber, uom, owner, ZonedDateTime.now());
  }

  public Coordinates readCoordinates(Scanner scanner) {
    out.println("Enter coordinates:");
    Integer x = readInteger(scanner, "  x (Integer, not empty):");
    Float y = readFloat(scanner, "  y (Float, not empty):", false, null);
    if (y == null) return null;
    return new Coordinates(x, y);
  }

  public Person readPerson(Scanner scanner) {
    out.println("Enter person:");
    String name = readString(scanner, "  Name (not empty):", false, 1, Integer.MAX_VALUE);
    if (name == null) return null;
    Date birthday = readDate(scanner, "  Birthday (empty or yyyy-MM-dd):");
    String passport =
        readString(scanner, "  Passport id (may be empty):", true, 0, Integer.MAX_VALUE);
    Location location = readLocation(scanner);
    if (location == null) return null;
    return new Person(name, birthday, passport, location);
  }

  public Location readLocation(Scanner scanner) {
    out.println("Enter location:");
    Float x = readFloat(scanner, "  x (Float, not empty):", false, null);
    if (x == null) return null;
    Integer y = readInteger(scanner, "  y (Integer, not empty):");
    Long z = readLong(scanner, "  z (Long, not empty):");
    String name = readString(scanner, "  Name (not empty, <=986):", false, 1, 986);
    if (name == null) return null;
    return new Location(x, y, z, name);
  }

  public UnitOfMeasure readUnitOfMeasure(Scanner scanner) {
    while (true) {
      out.println("Chose from: " + Arrays.toString(UnitOfMeasure.values()));
      out.print("  Unit of measure: ");
      out.flush();
      String line = scanner.nextLine().trim();
      if (line.isEmpty()) {
        out.println("Can't be empty.");
        continue;
      }
      try {
        return UnitOfMeasure.valueOf(line.toUpperCase());
      } catch (IllegalArgumentException e) {
        out.println("Wrong value.");
      }
    }
  }

  private String readString(
      Scanner scanner, String prompt, boolean nullable, int minLength, int maxLength) {
    while (true) {
      out.print(prompt + " ");
      out.flush();
      String line = scanner.nextLine().trim();
      if (line.isEmpty()) {
        if (nullable) return null;
        out.println("Can't be empty.");
        continue;
      }
      if (line.length() < minLength) {
        out.println("Must be longer than " + minLength);
        continue;
      }
      if (line.length() > maxLength) {
        out.println("Must be shorter than " + maxLength);
        continue;
      }
      return line;
    }
  }

  private Integer readInteger(Scanner scanner, String prompt) {
    while (true) {
      out.print(prompt + " ");
      out.flush();
      String line = scanner.nextLine().trim();
      if (line.isEmpty()) {
        out.println("Can't be empty.");
        continue;
      }
      try {
        return Integer.parseInt(line);
      } catch (NumberFormatException e) {
        out.println("Wrong format.");
      }
    }
  }

  private Float readFloat(Scanner scanner, String prompt, boolean nullable, Float min) {
    while (true) {
      out.print(prompt + " ");
      out.flush();
      String line = scanner.nextLine().trim();
      if (line.isEmpty()) {
        if (nullable) return null;
        out.println("Can't be empty.");
        continue;
      }
      try {
        float val = Float.parseFloat(line);
        if (!Float.isFinite(val)) {
          out.println("The number must be finite.");
          continue;
        }
        if (min != null && val <= min) {
          out.println("Must be greater than " + min);
          continue;
        }
        return val;
      } catch (NumberFormatException e) {
        out.println("Wrong format.");
      }
    }
  }

  private Long readLong(Scanner scanner, String prompt) {
    while (true) {
      out.print(prompt + " ");
      out.flush();
      String line = scanner.nextLine().trim();
      if (line.isEmpty()) {
        out.println("Can't be empty.");
        continue;
      }
      try {
        return Long.parseLong(line);
      } catch (NumberFormatException e) {
        out.println("Wrong format.");
      }
    }
  }

  private Date readDate(Scanner scanner, String prompt) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    sdf.setLenient(false);
    while (true) {
      out.print(prompt + " ");
      out.flush();
      String line = scanner.nextLine().trim();
      if (line.isEmpty()) return null;
      try {
        return sdf.parse(line);
      } catch (ParseException e) {
        out.println("Wrong format. Waiting yyyy-MM-dd.");
      }
    }
  }
}
