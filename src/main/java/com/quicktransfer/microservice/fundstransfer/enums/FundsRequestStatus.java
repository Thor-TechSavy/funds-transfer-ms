package com.quicktransfer.microservice.fundstransfer.enums;

public enum FundsRequestStatus {

    SUCCESSFUL("successful"),
    PROCESSING("processing"),
    PENDING("pending"),
    FAILED("failed");

    public final String value;

    FundsRequestStatus(String value) {
        this.value = value;
    }

    public String getStatus() {
        return value;
    }
}
