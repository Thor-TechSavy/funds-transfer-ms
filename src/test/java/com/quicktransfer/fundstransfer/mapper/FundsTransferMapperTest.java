package com.quicktransfer.fundstransfer.mapper;

import com.quicktransfer.fundstransfer.dto.FundsTransferRequestDto;
import com.quicktransfer.fundstransfer.entity.FundsTransferEntity;
import com.quicktransfer.fundstransfer.enums.FundsRequestStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FundsTransferMapperTest {

    @Test
    void testMapToDto() {
        // Arrange
        var entity = new FundsTransferEntity();
        entity.setRequestIdentifier("test-request-identifier");
        entity.setStatus(FundsRequestStatus.PENDING);

        // Act
        var dto = FundsTransferMapper.mapToDto(entity);

        // Assert
        assertEquals(entity.getRequestIdentifier(), dto.getRequestIdentifier());
        assertEquals(entity.getStatus(), dto.getStatus());
        assertEquals(entity.getFundsTransferRequestUUID(), dto.getFundsTransferRequestUUID());
    }

    @Test
    void testMapToEntity() {
        // Arrange
        var requestDto = new FundsTransferRequestDto();
        requestDto.setAmount(new BigDecimal("1000"));
        requestDto.setFromOwnerId(UUID.randomUUID());
        requestDto.setToOwnerId(UUID.randomUUID());

        // Act
        FundsTransferEntity entity = FundsTransferMapper.mapToEntity(requestDto);

        // Assert
        assertEquals(requestDto.getAmount(), entity.getAmount());
        assertEquals(requestDto.getFromOwnerId(), entity.getFromOwnerID());
        assertEquals(requestDto.getToOwnerId(), entity.getToOwnerID());
        assertEquals(FundsRequestStatus.PENDING, entity.getStatus());
    }
}
