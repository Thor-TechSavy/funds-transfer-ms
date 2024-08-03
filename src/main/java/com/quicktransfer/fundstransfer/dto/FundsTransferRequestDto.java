package com.quicktransfer.fundstransfer.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

public class FundsTransferRequestDto {

    @Schema(description = "uuid of the owner whose account is to be debited", requiredMode = Schema.RequiredMode.REQUIRED, example = "111111111111-1111-1111-1111-11111111")
    private final UUID fromOwnerId;

    @Schema(description = "uuid of the owner whose account is to be credited", requiredMode = Schema.RequiredMode.REQUIRED, example = "111111111111-1111-1111-1111-11111111")
    private final UUID toOwnerId;

    @Schema(description = "transaction amount", requiredMode = Schema.RequiredMode.REQUIRED, example = "500.0")
    private final BigDecimal amount;

    public FundsTransferRequestDto(final UUID fromOwnerId, final UUID toOwnerId, final BigDecimal amount) {
        this.fromOwnerId = fromOwnerId;
        this.toOwnerId = toOwnerId;
        this.amount = amount;
    }

    public UUID getFromOwnerId() {
        return fromOwnerId;
    }

    public UUID getToOwnerId() {
        return toOwnerId;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
