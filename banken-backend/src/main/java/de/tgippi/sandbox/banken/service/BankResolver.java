package de.tgippi.sandbox.banken.service;

import de.tgippi.sandbox.banken.persistence.BankEntity;

import java.util.Optional;

public interface BankResolver {
    Optional<BankEntity> findeBankFuerIban(String iban);
}
