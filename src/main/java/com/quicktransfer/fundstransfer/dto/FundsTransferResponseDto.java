package com.quicktransfer.fundstransfer.dto;

import com.quicktransfer.fundstransfer.enums.FundsRequestStatus;

import java.util.UUID;

public class FundsTransferResponseDto {

    private FundsRequestStatus status;

    private UUID fundsTransferRequestUUID;

    private String requestIdentifier;

    public FundsRequestStatus getStatus() {
        return status;
    }

    public void setStatus(FundsRequestStatus status) {
        this.status = status;
    }

    public UUID getFundsTransferRequestUUID() {
        return fundsTransferRequestUUID;
    }

    public void setFundsTransferRequestUUID(UUID fundsTransferRequestUUID) {
        this.fundsTransferRequestUUID = fundsTransferRequestUUID;
    }

    public String getRequestIdentifier() {
        return requestIdentifier;
    }

    public void setRequestIdentifier(String requestIdentifier) {
        this.requestIdentifier = requestIdentifier;
    }
}
