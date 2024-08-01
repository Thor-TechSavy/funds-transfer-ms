package com.quicktransfer.microservice.fundstransfer.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class FundsTransferRequestDto {

    private final UUID fromAccount;

    private final UUID toAccount;

    private final BigDecimal amount;

    public FundsTransferRequestDto(final UUID fromAccount, final UUID toAccount, final BigDecimal amount) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
    }

    public UUID getFromAccount() {
        return fromAccount;
    }

    public UUID getToAccount() {
        return toAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
