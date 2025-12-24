package ru.shortystory.items;

import ru.shortystory.characters.Shorty;
import ru.shortystory.records.NewsReport;
import ru.shortystory.exceptions.ItemMalfunctionException;

import java.util.Objects;

public abstract class AbstractItem {
    private String name;

    public AbstractItem(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract NewsReport use(Shorty user, Shorty target) throws ItemMalfunctionException;

    @Override
    public String toString() {
        return "AbstractItem{name='" + name + "'}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractItem item = (AbstractItem) o;
        return Objects.equals(name, item.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}