package ru.shortystory.exceptions;

public class SocialCreditBankruptcyException extends RuntimeException {
    public SocialCreditBankruptcyException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "КРИТИЧЕСКОЕ ПАДЕНИЕ РЕПУТАЦИИ: " + super.getMessage();
    }
}