package de.tgippi.sandbox.banken.service.bank;

import de.tgippi.sandbox.banken.persistence.BankEntity;
import de.tgippi.sandbox.banken.repository.BankRepository;
import de.tgippi.sandbox.banken.service.iban.Iban;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
class DefaultBankService implements BankService {

    private BankRepository bankRepository;

    private List<BankResolver> bankResolvers;

    @Override
    @Transactional
    public UUID erstelleBank(final String name, final String bic, final String blz) {

        var bankId = UUID.randomUUID();
        var bank = new BankEntity();
        bank.setBlz(blz);
        bank.setName(name);
        bank.setBic(bic);
        bankRepository.save(bank);

        return bankId;
    }

    @Override
    public Iterable<BankEntity> alleBanken() {
        return bankRepository.findAll();
    }

    @Override
    public Optional<BankEntity> findeBankFuerIban(final Iban iban) {
        return bankResolvers.stream()
                .map(bankResolver -> bankResolver.findeBankFuerIban(iban))
                .filter(Optional::isPresent)
                .map(Optional::get).findFirst();
    }

    @Autowired
    public void setBankRepository(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    @Autowired
    public void setBankResolvers(List<BankResolver> bankResolvers) {
        this.bankResolvers = bankResolvers;
    }
}
