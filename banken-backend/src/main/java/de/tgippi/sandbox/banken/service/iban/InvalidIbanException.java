package de.tgippi.sandbox.banken.service.iban;

public class InvalidIbanException extends Exception {
    public InvalidIbanException(String message) { super(message); }
}
