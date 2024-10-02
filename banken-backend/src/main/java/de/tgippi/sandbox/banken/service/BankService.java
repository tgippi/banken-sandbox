package de.tgippi.sandbox.banken.service;

import de.tgippi.sandbox.banken.persistence.BankEntity;

import java.util.Optional;
import java.util.UUID;

public interface BankService {

    UUID erstelleBank(String name, String bic, String blz);
    Iterable<BankEntity> alleBanken();
    Optional<BankEntity> findeBankFuerIban(String iban);
}
