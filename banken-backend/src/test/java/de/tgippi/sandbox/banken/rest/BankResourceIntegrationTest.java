package de.tgippi.sandbox.banken.rest;

import de.tgippi.sandbox.banken.AbstractIntegrationTest;
import de.tgippi.sandbox.banken.Bank;
import de.tgippi.sandbox.banken.IbanRequest;
import de.tgippi.sandbox.banken.IbanResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class BankResourceIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private TestRestTemplate template;

    @LocalServerPort
    private int port;

    private String baseUrl;

    @BeforeEach
    public void setup() {
        baseUrl = String.format("http://localhost:%d/api/v1", port);
    }

    @Test
    public void initialdatenTest() {
        assertThat(getBanken()).hasSizeGreaterThan(10);
    }

    @Test
    public void createBank_200() {
        Bank sparkasseRecklinghausen = new Bank(
                "Sparkasse Recklinghausen", "WELADED1REK", "42650150"
        );
        var res = template.exchange(baseUrl + "/banken", HttpMethod.PUT,
                new HttpEntity<>(sparkasseRecklinghausen), Object.class);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);

        Bank[] banken = getBanken();
        assertThat(banken).anySatisfy(bank -> {
            assertThat(bank.getName()).isEqualTo(sparkasseRecklinghausen.getName());
            assertThat(bank.getBic()).isEqualTo(sparkasseRecklinghausen.getBic());
            assertThat(bank.getBlz()).isEqualTo(sparkasseRecklinghausen.getBlz());
        });
    }

    @Test
    public void updateBank_200() {
        Bank sparkasseRecklinghausen = new Bank(
                "Sparkasse Recklinghausen", "WELADED1REK", "42650150"
        );
        var res = template.exchange(baseUrl + "/banken", HttpMethod.PUT,
                new HttpEntity<>(sparkasseRecklinghausen), Object.class);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);

        Bank sparkasseSchrecklinghausen = new Bank(
                "Sparkasse Schrecklinghausen", "WELADED1REK", "42650150"
        );
        var resUpdate = template.exchange(baseUrl + "/banken", HttpMethod.PUT,
                new HttpEntity<>(sparkasseSchrecklinghausen), Object.class);
        assertThat(resUpdate.getStatusCode()).isEqualTo(HttpStatus.OK);

        Bank[] banken = getBanken();
        assertThat(banken).anySatisfy(bank -> {
            assertThat(bank.getName()).isEqualTo(sparkasseSchrecklinghausen.getName());
            assertThat(bank.getBic()).isEqualTo(sparkasseSchrecklinghausen.getBic());
            assertThat(bank.getBlz()).isEqualTo(sparkasseRecklinghausen.getBlz());
        });
        assertThat(banken).noneSatisfy(bank -> {
            assertThat(bank.getName()).isEqualTo(sparkasseRecklinghausen.getName());
        });
    }

    @Test
    public void createBank_400() {
        Bank bank = new Bank("", "WELADED1REK", "42650150");
        var res = template.exchange(baseUrl + "/banken", HttpMethod.PUT,
                new HttpEntity<>(bank), Object.class);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void validateInvalidIban_200() {
        var iban = "123";
        var res = template.postForEntity(baseUrl + "/iban", new IbanRequest(iban), IbanResponse.class);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        IbanResponse ibanResponse = res.getBody();
        assertThat(ibanResponse.getIban()).isEqualTo(iban);
        assertThat(ibanResponse.getIsValid()).isFalse();
        assertThat(ibanResponse.getBank()).isNull();
    }

    @Test
    public void validateValidIban_200() {
        // https://ibanvalidieren.de/beispiele.html
        var iban = "DE02120300000000202051";
        var res = template.postForEntity(baseUrl + "/iban", new IbanRequest(iban), IbanResponse.class);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        IbanResponse ibanResponse = res.getBody();
        assertThat(ibanResponse.getIban()).isEqualTo(iban);
        assertThat(ibanResponse.getIsValid()).isTrue();
    }

    @Test
    public void validateValidIban_getBank_200() {
        // https://ibanvalidieren.de/beispiele.html
        var iban = "DE02120300000000202051";

        Bank dkb = new Bank("Deutsche Kreditbank Berlin", "BYLADEM1001", "12030000");
        var res = template.exchange(baseUrl + "/banken", HttpMethod.PUT,
                new HttpEntity<>(dkb), Object.class);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);

        var ibanRes = template.postForEntity(baseUrl + "/iban", new IbanRequest(iban), IbanResponse.class);
        assertThat(ibanRes.getStatusCode()).isEqualTo(HttpStatus.OK);
        IbanResponse ibanResponse = ibanRes.getBody();
        assertThat(ibanResponse.getIban()).isEqualTo(iban);
        assertThat(ibanResponse.getIsValid()).isTrue();
        assertThat(ibanResponse.getBank()).isNotNull();
        assertThat(ibanResponse.getBank()).satisfies(bank -> {
            assertThat(bank.getName()).isEqualTo(dkb.getName());
            assertThat(bank.getBic()).isEqualTo(dkb.getBic());
            assertThat(bank.getBlz()).isEqualTo(dkb.getBlz());
        });
    }

    private Bank[] getBanken() {
        ResponseEntity<Bank[]> response = template.getForEntity(baseUrl + "/banken", Bank[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        return response.getBody();
    }
}
