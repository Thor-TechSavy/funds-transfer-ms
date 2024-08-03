package com.quicktransfer.fundstransfer.enums;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    private static final Map<String, FundsRequestStatus> STATUS_MAP =
            Stream.of(values()).collect(Collectors.toMap(FundsRequestStatus::getStatus, Function.identity()));

    public static FundsRequestStatus fromString(String status) {
        return STATUS_MAP.get(status);
    }
}
