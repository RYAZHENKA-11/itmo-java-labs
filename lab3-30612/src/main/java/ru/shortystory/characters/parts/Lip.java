package ru.shortystory.characters.parts;

import ru.shortystory.interfaces.Malleable;

import java.util.Objects;

public class Lip implements Malleable {
    private double length;
    private double targetLength;

    public Lip(double length) {
        this.length = length;
        targetLength = length;
    }

    @Override
    public void stretch(double amount) {
        length += amount;
    }

    @Override
    public void compress(double amount) {
        if (length - amount < 0) {
            length = 0;
        } else {
            length -= amount;
        }
    }

    @Override
    public double getCurrentLength() {
        return length;
    }

    @Override
    public double getTargetLength() {
        return targetLength;
    }

    @Override
    public void setTargetLength(double length) {
        targetLength = length;
    }

    @Override
    public String toString() {
        return String.format("Lip{length=%.2f мм, targetLength=%.2f мм}", length, targetLength);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lip lip = (Lip) o;
        return Double.compare(lip.length, length) == 0 &&
                Double.compare(lip.targetLength, targetLength) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(length, targetLength);
    }
}