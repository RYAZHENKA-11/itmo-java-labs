package ru.shortystory.interfaces;

public interface Malleable {
    void stretch(double amount);

    void compress(double amount);

    double getCurrentLength();

    double getTargetLength();

    void setTargetLength(double length);
}