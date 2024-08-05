package com.quicktransfer.fundstransfer.entity;

import com.quicktransfer.fundstransfer.enums.FundsRequestStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "FUNDS_TRANSFER_REQUEST")
public class FundsTransferEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "requestId", nullable = false, unique = true)
    private final UUID fundsTransferRequestUUID = UUID.randomUUID();

    @Column(name = "fromOwnerId", nullable = false)
    private UUID fromOwnerID;

    @Column(name = "toOwnerId", nullable = false)
    private UUID toOwnerID;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "requestIdentifier", nullable = false, unique = true)
    private String requestIdentifier;

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

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequestIdentifier() {
        return requestIdentifier;
    }

    public void setRequestIdentifier(String requestIdentifier) {
        this.requestIdentifier = requestIdentifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FundsTransferEntity that = (FundsTransferEntity) o;
        return Objects.equals(fundsTransferRequestUUID, that.fundsTransferRequestUUID) && Objects.equals(fromOwnerID, that.fromOwnerID) && Objects.equals(toOwnerID, that.toOwnerID) && Objects.equals(requestIdentifier, that.requestIdentifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fundsTransferRequestUUID, fromOwnerID, toOwnerID, requestIdentifier);
    }

    @Override
    public String toString() {
        return "FundsTransferEntity{" +
                "id=" + id +
                ", fundsTransferRequestUUID=" + fundsTransferRequestUUID +
                ", fromOwnerID=" + fromOwnerID +
                ", toOwnerID=" + toOwnerID +
                ", amount=" + amount +
                ", requestIdentifier='" + requestIdentifier + '\'' +
                ", status=" + status +
                ", creationTime=" + creationTime +
                ", lastUpdateTime=" + lastUpdateTime +
                '}';
    }

    // hash and equals
}
