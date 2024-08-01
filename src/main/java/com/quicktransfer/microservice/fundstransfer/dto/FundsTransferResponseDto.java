package com.quicktransfer.microservice.fundstransfer.dto;

import com.quicktransfer.microservice.fundstransfer.enums.FundsRequestStatus;

import java.util.UUID;

public class FundsTransferResponseDto {

    private FundsRequestStatus status;

    private UUID fundsTransferRequestUUID;

    private String request;

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

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }
}
