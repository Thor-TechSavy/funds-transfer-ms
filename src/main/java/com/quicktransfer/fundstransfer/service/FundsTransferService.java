package com.quicktransfer.fundstransfer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.quicktransfer.fundstransfer.client.AccountClient;
import com.quicktransfer.fundstransfer.client.TransactionRequest;
import com.quicktransfer.fundstransfer.dto.FundsTransferRequestDto;
import com.quicktransfer.fundstransfer.entity.FundsTransferEntity;
import com.quicktransfer.fundstransfer.enums.FundsRequestStatus;
import com.quicktransfer.fundstransfer.repository.FundsTransferRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FundsTransferService {

    private final AccountClient accountClient;
    private final FundsTransferRepository fundsTransferRepository;

    public FundsTransferService(AccountClient accountClient,
                                FundsTransferRepository fundsTransferRepository) {
        this.accountClient = accountClient;
        this.fundsTransferRepository = fundsTransferRepository;
    }


    public FundsTransferEntity createFundsTransferRequest(FundsTransferRequestDto requestDto) {


        FundsTransferEntity fundsTransferEntity = new FundsTransferEntity();
        fundsTransferEntity.setAmount(requestDto.getAmount());
        fundsTransferEntity.setFromOwnerID(requestDto.getFromOwnerId());
        fundsTransferEntity.setToOwnerID(requestDto.getToOwnerId());
        fundsTransferEntity.setStatus(FundsRequestStatus.PENDING);

        return fundsTransferRepository.save(fundsTransferEntity);

    }

    @Transactional
    public void processFundsTransfer() {

        FundsTransferEntity requestEntity = fundsTransferRepository
                .findFirstByStatusOrderByCreationTimeAsc(FundsRequestStatus.PENDING)
                .orElseThrow(() -> new RuntimeException("sf"));

        requestEntity.setStatus(FundsRequestStatus.PROCESSING);
        fundsTransferRepository.save(requestEntity);

        TransactionRequest transactionRequest = mapToRequest(requestEntity);

        accountClient.performDebitAndCreditOperations(transactionRequest);

        requestEntity.setStatus(FundsRequestStatus.SUCCESSFUL);
        fundsTransferRepository.save(requestEntity);

    }

    private TransactionRequest mapToRequest(FundsTransferEntity requestEntity) {
        TransactionRequest transactionRequest = new TransactionRequest();

        transactionRequest.setAmount(requestEntity.getAmount());
        transactionRequest.setFromOwnerId(requestEntity.getFromOwnerID());
        transactionRequest.setToOwnerId(requestEntity.getToOwnerID());
        transactionRequest.setCalleeName("FUND-MS");
        transactionRequest.setTransferRequestUUID(transactionRequest.getTransferRequestUUID());

        return transactionRequest;
    }
}
