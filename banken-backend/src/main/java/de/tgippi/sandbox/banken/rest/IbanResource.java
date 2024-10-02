package de.tgippi.sandbox.banken.rest;

import de.tgippi.sandbox.banken.IbanApi;
import de.tgippi.sandbox.banken.IbanRequest;
import de.tgippi.sandbox.banken.IbanResponse;
import de.tgippi.sandbox.banken.service.BankService;
import de.tgippi.sandbox.banken.service.IbanValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IbanResource implements IbanApi {

    @Autowired
    private IbanValidatorService ibanValidatorService;

    @Autowired
    private BankService bankService;

    @Override
    public ResponseEntity<IbanResponse> pruefeIban(IbanRequest ibanRequest) {
        final String iban = ibanRequest.getIban().replaceAll("\\s+", "");;
        if (!ibanValidatorService.isValidIban(iban))
            return ResponseEntity.ok(new IbanResponse(iban, false));

        return ResponseEntity.ok(erstelleResponse(iban));
    }

    private IbanResponse erstelleResponse(String iban) {
        final IbanResponse response = new IbanResponse(iban, true);
        bankService.findeBankFuerIban(iban).ifPresent((bank) -> {
            response.setBank(BankConverter.entityToDto(bank));
        });
        return response;
    }
}
