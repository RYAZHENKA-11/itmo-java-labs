package ru.shortystory.items;

import java.util.Objects;

public class Clothing {
    private final String type;
    private final boolean isFashionable;

    public Clothing(String type, boolean isFashionable) {
        this.type = type;
        this.isFashionable = isFashionable;
    }

    public String getType() {
        return type;
    }

    public boolean isFashionable() {
        return isFashionable;
    }

    @Override
    public String toString() {
        return "Clothing{" +
                "type='" + type + "'" +
                ", isFashionable=" + isFashionable +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Clothing clothing = (Clothing) o;
        return isFashionable == clothing.isFashionable &&
                Objects.equals(type, clothing.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, isFashionable);
    }
}