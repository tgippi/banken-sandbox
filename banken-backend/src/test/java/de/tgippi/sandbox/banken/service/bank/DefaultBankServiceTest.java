package de.tgippi.sandbox.banken.service.bank;

import de.tgippi.sandbox.banken.persistence.BankEntity;
import de.tgippi.sandbox.banken.repository.BankRepository;
import de.tgippi.sandbox.banken.service.iban.Iban;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class DefaultBankServiceTest {

    BankRepository bankRepository;
    BankResolver bankResolver;

    DefaultBankService sut = new DefaultBankService();

    @BeforeEach
    public void setup() {
        bankRepository = Mockito.mock(BankRepository.class);
        bankResolver = Mockito.mock(BankResolver.class);

        sut.setBankRepository(bankRepository);
        sut.setBankResolvers(List.of(bankResolver));
    }

    @Test
    public void test_erstelleBank() {
        sut.erstelleBank("Deutsche Bank", "BYLADEM1001", "12030000");
        verify(bankRepository).save(argThat((b) -> b.getName().equals("Deutsche Bank")
                && b.getBic().equals("BYLADEM1001")
                && b.getBlz().equals("12030000")));
    }


    @Test
    public void test_findeBankFuerGueltigeDeutscheIban() {
        var iban = Mockito.mock(Iban.class);
        when(iban.getValue()).thenReturn("DE02120300000000202051");
        var bank = Mockito.mock(BankEntity.class);
        when(bankResolver.findeBankFuerIban(iban)).thenReturn(Optional.of(bank));
        assertThat(sut.findeBankFuerIban(iban)).hasValue(bank);
    }

    @Test
    public void test_findeKeineBankFuerGueltigeDeutscheIban() {
        var iban = Mockito.mock(Iban.class);
        when(iban.getValue()).thenReturn("DE02120300000000202051");
        when(bankResolver.findeBankFuerIban(iban)).thenReturn(Optional.empty());
        assertThat(sut.findeBankFuerIban(iban)).isEmpty();
    }
}
