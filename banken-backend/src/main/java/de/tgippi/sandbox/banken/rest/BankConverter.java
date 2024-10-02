package de.tgippi.sandbox.banken.rest;

import de.tgippi.sandbox.banken.Bank;
import de.tgippi.sandbox.banken.persistence.BankEntity;

public abstract class BankConverter {
    public static Bank entityToDto(final BankEntity entity) {
        return new Bank(entity.getName(), entity.getBic(), entity.getBlz());
    }
}
