package com.quicktransfer.microservice.fundstransfer.controller;

import com.quicktransfer.microservice.fundstransfer.dto.FundsTransferRequestDto;
import com.quicktransfer.microservice.fundstransfer.dto.FundsTransferResponseDto;
import com.quicktransfer.microservice.fundstransfer.entity.FundsTransferEntity;
import com.quicktransfer.microservice.fundstransfer.service.FundsTransferService;
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
        responseDto.setRequest(entity.getRequest());
        responseDto.setStatus(entity.getStatus());
        responseDto.setFundsTransferRequestUUID(entity.getFundsTransferRequestUUID());

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }
}
