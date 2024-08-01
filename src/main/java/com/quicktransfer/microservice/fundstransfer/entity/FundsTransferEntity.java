package com.quicktransfer.microservice.fundstransfer.entity;

import com.quicktransfer.microservice.fundstransfer.enums.FundsRequestStatus;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
public class FundsTransferEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "txnId", nullable = false)
    private final UUID fundsTransferRequestUUID = UUID.randomUUID();

    @Column(name = "request", nullable = false)
    private String request;

    @Column(name = "txnId", nullable = false)
    private FundsRequestStatus status;

    @Column(name = "creationTime", nullable = false)
    private final Instant creationTime = Instant.now();

    @Column(name = "lastUpdateTime")
    private Instant lastUpdateTime;

    public Long getId() {
        return id;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public UUID getFundsTransferRequestUUID() {
        return fundsTransferRequestUUID;
    }

    public FundsRequestStatus getStatus() {
        return status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStatus(FundsRequestStatus status) {
        this.status = status;
    }

    public Instant getCreationTime() {
        return creationTime;
    }

    public Instant getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Instant lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    // hash and equals
}
