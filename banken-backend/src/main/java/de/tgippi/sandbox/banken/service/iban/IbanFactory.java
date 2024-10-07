package de.tgippi.sandbox.banken.service.iban;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IbanFactory {

    private IbanValidatorService ibanValidatorService;

    public Iban erstelleIbanFuerString(String ibanString) throws InvalidIbanException {
        final Iban iban = new Iban(ibanString);
        if (!ibanValidatorService.istGueltigeIban(iban.getValue())) {
            throw new InvalidIbanException(String.format("Ungültige Iban %s", ibanString));
        }
        return iban;
    }

    @Autowired
    public void setIbanValidatorService(IbanValidatorService ibanValidatorService) {
        this.ibanValidatorService = ibanValidatorService;
    }
}
