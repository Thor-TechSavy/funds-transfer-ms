package com.quicktransfer.fundstransfer.controller;

import com.quicktransfer.fundstransfer.dto.FundsTransferRequestDto;
import com.quicktransfer.fundstransfer.dto.FundsTransferResponseDto;
import com.quicktransfer.fundstransfer.entity.FundsTransferEntity;
import com.quicktransfer.fundstransfer.service.FundsTransferService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/funds-transfer")
public class FundsTransferController {

    private final FundsTransferService fundsTransferService;

    public FundsTransferController(FundsTransferService fundsTransferService) {
        this.fundsTransferService = fundsTransferService;
    }

    @PostMapping
    public ResponseEntity<FundsTransferResponseDto> createFundsTransferRequest(@RequestBody FundsTransferRequestDto requestDto) {

        FundsTransferEntity entity = fundsTransferService.createFundsTransferRequest(requestDto);

        FundsTransferResponseDto responseDto = new FundsTransferResponseDto();
        responseDto.setRequest(entity.getRequestIdentifier());
        responseDto.setStatus(entity.getStatus());
        responseDto.setFundsTransferRequestUUID(entity.getFundsTransferRequestUUID());

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }
}
