package de.tgippi.sandbox.banken.service;

import de.tgippi.sandbox.banken.persistence.BankEntity;
import de.tgippi.sandbox.banken.repository.BankRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class DeutscheIbanBankResolverTest {

    BankRepository bankRepository;

    DeutscheIbanBankResolver sut = new DeutscheIbanBankResolver();

    @BeforeEach
    public void setup() {
        bankRepository = Mockito.mock(BankRepository.class);
        sut.setBankRepository(bankRepository);
    }

    @Test
    public void test_findBankFuerDeutscheIban() {
        var iban = "DE02120300000000202051";
        var bank = Mockito.mock(BankEntity.class);
        when(bankRepository.findByBlz("12030000")).thenReturn(List.of(bank));
        assertThat(sut.findeBankFuerIban(iban)).isPresent();
    }

    @Test
    public void test_findBankFuerAuslaendischeIban() {
        var iban = "EN02120300000000202051";
        verifyNoInteractions(bankRepository);
        assertThat(sut.findeBankFuerIban(iban)).isEmpty();
    }
}
