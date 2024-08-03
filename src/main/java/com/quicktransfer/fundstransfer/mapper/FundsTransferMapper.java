package com.quicktransfer.fundstransfer.mapper;

import com.quicktransfer.fundstransfer.dto.FundsTransferRequestDto;
import com.quicktransfer.fundstransfer.dto.FundsTransferResponseDto;
import com.quicktransfer.fundstransfer.entity.FundsTransferEntity;
import com.quicktransfer.fundstransfer.enums.FundsRequestStatus;

public class FundsTransferMapper {

    private FundsTransferMapper() {
    }

    public static FundsTransferResponseDto mapToDto(FundsTransferEntity entity) {

        FundsTransferResponseDto responseDto = new FundsTransferResponseDto();
        responseDto.setRequestIdentifier(entity.getRequestIdentifier());
        responseDto.setStatus(entity.getStatus());
        responseDto.setFundsTransferRequestUUID(entity.getFundsTransferRequestUUID());

        return responseDto;
    }
    public static FundsTransferEntity mapToEntity(FundsTransferRequestDto requestDto) {
        FundsTransferEntity fundsTransferEntity = new FundsTransferEntity();
        fundsTransferEntity.setAmount(requestDto.getAmount());
        fundsTransferEntity.setFromOwnerID(requestDto.getFromOwnerId());
        fundsTransferEntity.setToOwnerID(requestDto.getToOwnerId());
        fundsTransferEntity.setStatus(FundsRequestStatus.PENDING);
        return fundsTransferEntity;
    }
}
