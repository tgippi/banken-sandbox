package de.tgippi.sandbox.banken.service.iban;

import java.util.Objects;

public final class Iban {

    private final String value;

    Iban(String iban) {
        this.value = sanitize(iban);
    }

    public String getValue() { return value; }

    private static String sanitize(String iban) {
        return iban.replaceAll("\\s+", "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Iban iban = (Iban) o;
        return Objects.equals(value, iban.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
