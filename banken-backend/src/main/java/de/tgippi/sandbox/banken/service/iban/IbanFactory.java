package de.tgippi.sandbox.banken.service.iban;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IbanFactory {

    private IbanValidatorService ibanValidatorService;

    public Iban erstelleIbanFuerString(String iban) throws InvalidIbanException {
        if (!ibanValidatorService.istGueltigeIban(iban)) {
            throw new InvalidIbanException(String.format("Ungültige Iban %s", iban));
        }
        return new Iban(iban);
    }

    @Autowired
    public void setIbanValidatorService(IbanValidatorService ibanValidatorService) {
        this.ibanValidatorService = ibanValidatorService;
    }
}
