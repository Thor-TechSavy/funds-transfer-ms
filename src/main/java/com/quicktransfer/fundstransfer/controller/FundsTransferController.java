package com.quicktransfer.fundstransfer.controller;

import com.quicktransfer.fundstransfer.dto.FundsTransferRequestDto;
import com.quicktransfer.fundstransfer.dto.FundsTransferResponseDto;
import com.quicktransfer.fundstransfer.entity.FundsTransferEntity;
import com.quicktransfer.fundstransfer.exception.FundsTransferException;
import com.quicktransfer.fundstransfer.service.FundsTransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.quicktransfer.fundstransfer.mapper.FundsTransferMapper.mapToDto;
import static com.quicktransfer.fundstransfer.mapper.FundsTransferMapper.mapToEntity;

@RestController
@RequestMapping("/v1/funds-transfer")
public class FundsTransferController {

    private final FundsTransferService fundsTransferService;

    public FundsTransferController(FundsTransferService fundsTransferService) {
        this.fundsTransferService = fundsTransferService;
    }

    @Operation(summary = "To create/initiate the funds transfer requests between the accounts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "funds transfer request created", content =
                    {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = FundsTransferResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "invalid request payload", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server error, the details are logged on "
                    + "backend", content = @Content)})
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<FundsTransferResponseDto> createFundsTransferRequest(@RequestBody FundsTransferRequestDto requestDto) {

        validateFundsTransferRequest(requestDto);
        FundsTransferEntity fundsTransferEntity = mapToEntity(requestDto);
        FundsTransferEntity entity = fundsTransferService.createFundsTransferRequest(fundsTransferEntity);

        return new ResponseEntity<>(mapToDto(entity), HttpStatus.CREATED);
    }

    @Operation(summary = "To retrieve the request by request UUID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "funds transfer request fetched successfully", content =
                    {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = FundsTransferResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "invalid request payload", content = @Content),
            @ApiResponse(responseCode = "404", description = "not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server error, the details are logged on "
                    + "backend", content = @Content)})
    @GetMapping(value = "/{uuid}", produces = "application/json")
    public ResponseEntity<FundsTransferResponseDto> getFundsTransferRequest(@PathVariable UUID uuid) {
        FundsTransferEntity entity = fundsTransferService.getFundsTransferRequest(uuid);

        return new ResponseEntity<>(mapToDto(entity), HttpStatus.OK);

    }

    private void validateFundsTransferRequest(final FundsTransferRequestDto request) {
        boolean isAmountNull = request.getAmount() == null;
        boolean isFromOwnerId = request.getFromOwnerId() == null;
        boolean isToOwnerId = request.getToOwnerId() == null;

        if (isAmountNull || isFromOwnerId || isToOwnerId) {
            throw new FundsTransferException("Invalid request");
        }
    }
}
