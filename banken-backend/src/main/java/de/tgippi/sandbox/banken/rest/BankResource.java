package de.tgippi.sandbox.banken.rest;

import de.tgippi.sandbox.banken.Bank;
import de.tgippi.sandbox.banken.BankApi;
import de.tgippi.sandbox.banken.service.bank.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.StreamSupport;

@RestController
public class BankResource implements BankApi {

    @Autowired
    private BankService bankService;

    @Override
    public ResponseEntity<Void> erstelleBank(final Bank bank) {
        bankService.erstelleBank(bank.getName(), bank.getBic(), bank.getBlz());
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<Bank>> alleBanken() {
        var banken = StreamSupport.stream(this.bankService.alleBanken().spliterator(), false)
                .map(BankConverter::entityToDto)
                .toList();
        return ResponseEntity.ok(banken);
    }
}
