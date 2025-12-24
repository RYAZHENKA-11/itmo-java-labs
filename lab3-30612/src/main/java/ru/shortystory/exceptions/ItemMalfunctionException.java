package ru.shortystory.exceptions;

public class ItemMalfunctionException extends Exception {
    public ItemMalfunctionException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "Ошибка предмета: " + super.getMessage();
    }
}