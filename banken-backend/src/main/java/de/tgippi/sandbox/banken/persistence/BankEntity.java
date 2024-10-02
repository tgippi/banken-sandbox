package de.tgippi.sandbox.banken.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity(name = "Bank")
public final class BankEntity {

    @Id
    private String blz;
    private String name;
    private String bic;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }

    public String getBlz() {
        return blz;
    }

    public void setBlz(String blz) {
        this.blz = blz;
    }
}
