package com.quicktransfer.fundstransfer.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("fundstransfer")

public class FundsTransferProperties {

    private String fetchLastSeconds;
    private String pendingRequestsScheduler;


    public String getFetchLastSeconds() {
        return fetchLastSeconds;
    }

    public void setFetchLastSeconds(String fetchLastSeconds) {
        this.fetchLastSeconds = fetchLastSeconds;
    }

    public String getPendingRequestsScheduler() {
        return pendingRequestsScheduler;
    }

    public void setPendingRequestsScheduler(String pendingRequestsScheduler) {
        this.pendingRequestsScheduler = pendingRequestsScheduler;
    }
}
