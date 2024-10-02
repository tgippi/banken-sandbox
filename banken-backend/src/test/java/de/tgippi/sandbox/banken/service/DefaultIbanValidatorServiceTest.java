package de.tgippi.sandbox.banken.service;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultIbanValidatorServiceTest {

    IbanValidatorService sut = new DefaultIbanValidatorService();

    @ParameterizedTest
    @ValueSource(strings = {
        // https://ibanvalidieren.de/beispiele.html
        "DE02120300000000202051",
        "AT026000000001349870",
        "CH0209000000100013997",
        "LI0208800000017197386",
    })
    void testValidateValidIban(String iban) {
        assertThat(sut.isValidIban(iban)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "123",
            "DE02120300000000202051a",
            "ABC02120300000000202051",
    })
    void testValidateInvalidIban(String iban) {
        assertThat(sut.isValidIban(iban)).isFalse();
    }
}
