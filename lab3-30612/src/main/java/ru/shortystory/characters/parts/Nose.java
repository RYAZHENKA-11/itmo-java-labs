package ru.shortystory.characters.parts;

import ru.shortystory.interfaces.Malleable;

import java.util.Objects;

public class Nose implements Malleable {
    private double length;
    private double targetLength;

    public Nose(double length) {
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
        return String.format("Hose{length=%.2f мм, targetLength=%.2f мм}", length, targetLength);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Nose nose = (Nose) o;
        return Double.compare(nose.length, length) == 0 &&
                Double.compare(nose.targetLength, targetLength) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(length, targetLength);
    }
}