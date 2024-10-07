package de.tgippi.sandbox.banken.service.bank;

import de.tgippi.sandbox.banken.persistence.BankEntity;
import de.tgippi.sandbox.banken.service.iban.Iban;

import java.util.Optional;

public interface BankResolver {
    Optional<BankEntity> findeBankFuerIban(Iban iban);
}
