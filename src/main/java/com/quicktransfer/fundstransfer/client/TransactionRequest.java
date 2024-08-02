package com.quicktransfer.fundstransfer.client;

import java.math.BigDecimal;
import java.util.UUID;

public class TransactionRequest {

    private UUID fromOwnerId;
    private UUID toOwnerId;
    private BigDecimal amount;
    private String calleeName;
    private String transferRequestCreationTime;
    private UUID transferRequestUUID;

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

    public String getCalleeName() {
        return calleeName;
    }

    public void setCalleeName(String calleeName) {
        this.calleeName = calleeName;
    }

    public String getTransferRequestCreationTime() {
        return transferRequestCreationTime;
    }

    public void setTransferRequestCreationTime(String transferRequestCreationTime) {
        this.transferRequestCreationTime = transferRequestCreationTime;
    }

    public UUID getTransferRequestUUID() {
        return transferRequestUUID;
    }

    public void setTransferRequestUUID(UUID transferRequestUUID) {
        this.transferRequestUUID = transferRequestUUID;
    }
}
