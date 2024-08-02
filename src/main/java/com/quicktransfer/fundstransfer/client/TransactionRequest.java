package com.quicktransfer.fundstransfer.client;

import java.math.BigDecimal;
import java.util.UUID;

public class TransactionRequest {

    private UUID fromOwnerId;
    private UUID toOwnerId;
    private BigDecimal amount;
    private RequestIdentifier requestIdentifier;

    public UUID getFromOwnerId() {
        return fromOwnerId;
    }

    public void setFromOwnerId(UUID fromOwnerId) {
        this.fromOwnerId = fromOwnerId;
    }

    public UUID getToOwnerId() {
        return toOwnerId;
    }

    public void setToOwnerId(UUID toOwnerId) {
        this.toOwnerId = toOwnerId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public RequestIdentifier getRequestIdentifier() {
        return requestIdentifier;
    }

    public void setRequestIdentifier(RequestIdentifier requestIdentifier) {
        this.requestIdentifier = requestIdentifier;
    }
}
