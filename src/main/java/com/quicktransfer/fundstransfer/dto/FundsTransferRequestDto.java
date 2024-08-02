package com.quicktransfer.fundstransfer.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class FundsTransferRequestDto {

    private final UUID fromOwnerId;

    private final UUID toOwnerId;

    private final BigDecimal amount;

    public FundsTransferRequestDto(final UUID fromOwnerId, final UUID toOwnerId, final BigDecimal amount) {
        this.fromOwnerId = fromOwnerId;
        this.toOwnerId = toOwnerId;
        this.amount = amount;
    }

    public UUID getFromOwnerId() {
        return fromOwnerId;
    }

    public UUID getToOwnerId() {
        return toOwnerId;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
