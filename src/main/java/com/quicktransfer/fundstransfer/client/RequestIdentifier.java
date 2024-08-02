package com.quicktransfer.fundstransfer.client;

import java.util.UUID;

public class RequestIdentifier {

    private String calleeName;
    private String requestTime;
    private UUID transferRequestId;

    public String getCalleeName() {
        return calleeName;
    }

    public void setCalleeName(String calleeName) {
        this.calleeName = calleeName;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public UUID getTransferRequestId() {
        return transferRequestId;
    }

    public void setTransferRequestId(UUID transferRequestId) {
        this.transferRequestId = transferRequestId;
    }
}
