package com.quicktransfer.microservice.fundstransfer.client.account;

import java.math.BigDecimal;
import java.util.UUID;

public class AccountResponse {

    private UUID ownerID;
    private BigDecimal balance;
    private String currency;

    public UUID getOwnerID() {
        return ownerID;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getCurrency() {
        return currency;
    }


}
