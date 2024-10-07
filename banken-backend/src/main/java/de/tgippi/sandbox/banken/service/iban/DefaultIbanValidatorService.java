package de.tgippi.sandbox.banken.service.iban;

import org.iban4j.IbanUtil;
import org.springframework.stereotype.Service;

@Service
class DefaultIbanValidatorService implements IbanValidatorService {
    @Override
    public boolean istGueltigeIban(String iban) {
        return IbanUtil.isValid(iban);
    }
}
