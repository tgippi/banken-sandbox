package de.tgippi.sandbox.banken.service;

import de.tgippi.sandbox.banken.persistence.BankEntity;
import de.tgippi.sandbox.banken.repository.BankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DeutscheIbanBankResolver implements BankResolver {

    private final int BLZ_START_INDEX = 4;
    private final int BLZ_LENGTH = 8;

    private BankRepository bankRepository;

    @Override
    public Optional<BankEntity> findeBankFuerIban(String iban) {
        if (!iban.startsWith("DE"))
            return Optional.empty();

        var blz = extrahiereBankleitzahl(iban);
        return bankRepository.findByBlz(blz).stream().findFirst();
    }

    private String extrahiereBankleitzahl(String iban) {
        return iban.substring(BLZ_START_INDEX, BLZ_START_INDEX + BLZ_LENGTH);
    }

    @Autowired
    public void setBankRepository(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

}
