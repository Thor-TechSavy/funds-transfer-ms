package com.quicktransfer.fundstransfer.entity;

import com.quicktransfer.fundstransfer.enums.FundsRequestStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
public class FundsTransferEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "requestId", nullable = false, unique = true)
    private final UUID fundsTransferRequestUUID = UUID.randomUUID();

    @Column(name = "fromOwnerId", nullable = false, unique = true)
    private UUID fromOwnerID;

    @Column(name = "toOwnerId", nullable = false, unique = true)
    private UUID toOwnerID;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private FundsRequestStatus status;

    @Column(name = "creationTime", nullable = false)
    private final Instant creationTime = Instant.now();

    @Column(name = "lastUpdateTime")
    private Instant lastUpdateTime;

    public Long getId() {
        return id;
    }

    public UUID getFundsTransferRequestUUID() {
        return fundsTransferRequestUUID;
    }

    public FundsRequestStatus getStatus() {
        return status;
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

    public UUID getFromOwnerID() {
        return fromOwnerID;
    }

    public void setFromOwnerID(UUID fromOwnerID) {
        this.fromOwnerID = fromOwnerID;
    }

    public UUID getToOwnerID() {
        return toOwnerID;
    }

    public void setToOwnerID(UUID toOwnerID) {
        this.toOwnerID = toOwnerID;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    // hash and equals
}
