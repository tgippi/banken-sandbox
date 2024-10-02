package de.tgippi.sandbox.banken.service;

import org.iban4j.IbanUtil;
import org.springframework.stereotype.Service;

@Service
public class DefaultIbanValidatorService implements IbanValidatorService {
    @Override
    public boolean isValidIban(String iban) {
        return IbanUtil.isValid(iban);
    }
}
