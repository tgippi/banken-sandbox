package de.tgippi.sandbox.banken.rest;

import de.tgippi.sandbox.banken.IbanApi;
import de.tgippi.sandbox.banken.IbanRequest;
import de.tgippi.sandbox.banken.IbanResponse;
import de.tgippi.sandbox.banken.service.bank.BankService;
import de.tgippi.sandbox.banken.service.iban.Iban;
import de.tgippi.sandbox.banken.service.iban.IbanFactory;
import de.tgippi.sandbox.banken.service.iban.InvalidIbanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IbanResource implements IbanApi {

    @Autowired
    private IbanFactory ibanFactory;

    @Autowired
    private BankService bankService;

    @Override
    public ResponseEntity<IbanResponse> pruefeIban(IbanRequest ibanRequest) {
        try {
            final Iban iban = ibanFactory.erstelleIbanFuerString(ibanRequest.getIban());
            return ResponseEntity.ok(erstelleResponse(iban));
        } catch (InvalidIbanException e) {
            return ResponseEntity.ok(new IbanResponse(ibanRequest.getIban(), false));
        }
    }

    private IbanResponse erstelleResponse(Iban iban) {
        final IbanResponse response = new IbanResponse(iban.getValue(), true);
        bankService.findeBankFuerIban(iban).ifPresent((bank) -> {
            response.setBank(BankConverter.entityToDto(bank));
        });
        return response;
    }
}
