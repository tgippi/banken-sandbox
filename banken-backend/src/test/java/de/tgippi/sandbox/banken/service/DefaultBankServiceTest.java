package de.tgippi.sandbox.banken.service;

import de.tgippi.sandbox.banken.persistence.BankEntity;
import de.tgippi.sandbox.banken.repository.BankRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class DefaultBankServiceTest {

    BankRepository bankRepository;
    IbanValidatorService ibanValidatorService;
    BankResolver bankResolver;

    DefaultBankService sut = new DefaultBankService();

    @BeforeEach
    public void setup() {
        bankRepository = Mockito.mock(BankRepository.class);
        ibanValidatorService = Mockito.mock(IbanValidatorService.class);
        bankResolver = Mockito.mock(BankResolver.class);

        sut.setBankRepository(bankRepository);
        sut.setIbanValidatorService(ibanValidatorService);
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
        var iban = "DE02120300000000202051";
        var bank = Mockito.mock(BankEntity.class);
        when(ibanValidatorService.isValidIban(iban)).thenReturn(true);
        when(bankResolver.findeBankFuerIban(iban)).thenReturn(Optional.of(bank));
        assertThat(sut.findeBankFuerIban(iban)).hasValue(bank);
    }

    @Test
    public void test_findeKeineBankFuerGueltigeDeutscheIban() {
        var iban = "DE02120300000000202051";
        when(ibanValidatorService.isValidIban(iban)).thenReturn(true);
        when(bankResolver.findeBankFuerIban(iban)).thenReturn(Optional.empty());
        assertThat(sut.findeBankFuerIban(iban)).isEmpty();
    }

    @Test
    public void test_findBankFuerUngueltigeIban() {
        var iban = "123";
        when(ibanValidatorService.isValidIban(iban)).thenReturn(false);
        verifyNoInteractions(bankRepository);
        assertThat(sut.findeBankFuerIban(iban)).isEmpty();
    }

}
