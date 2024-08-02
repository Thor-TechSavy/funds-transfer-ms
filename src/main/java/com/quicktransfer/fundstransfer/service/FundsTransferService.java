package com.quicktransfer.fundstransfer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.quicktransfer.fundstransfer.client.AccountClient;
import com.quicktransfer.fundstransfer.client.RequestIdentifier;
import com.quicktransfer.fundstransfer.client.TransactionRequest;
import com.quicktransfer.fundstransfer.dto.FundsTransferRequestDto;
import com.quicktransfer.fundstransfer.entity.FundsTransferEntity;
import com.quicktransfer.fundstransfer.enums.FundsRequestStatus;
import com.quicktransfer.fundstransfer.exception.FundsTransferException;
import com.quicktransfer.fundstransfer.repository.FundsTransferRepository;
import com.quicktransfer.fundstransfer.util.JsonUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

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

        String requestIdentifier = createRequestIdentifier(fundsTransferEntity);
        fundsTransferEntity.setRequestIdentifier(requestIdentifier);

        return fundsTransferRepository.save(fundsTransferEntity);

    }

    private static String createRequestIdentifier(FundsTransferEntity fundsTransferEntity) {
        RequestIdentifier identifier = new RequestIdentifier();
        identifier.setCalleeName("FUND-MS");
        identifier.setRequestTime(String.valueOf(fundsTransferEntity.getCreationTime()));
        identifier.setTransferRequestId(fundsTransferEntity.getFundsTransferRequestUUID());

        try {
            return JsonUtil.getMapper().writeValueAsString(identifier);
        } catch (JsonProcessingException e) {
            throw new FundsTransferException(e.getMessage());
        }
    }

    @Transactional
    public void processFundsTransfer() {

        List<FundsTransferEntity> requestEntities = fundsTransferRepository
                .findFirst10ByStatusOrderByCreationTimeAsc(FundsRequestStatus.PENDING);

        requestEntities.forEach(this::processTransferRequest);

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processTransferRequest(FundsTransferEntity requestEntity) {

        requestEntity.setStatus(FundsRequestStatus.PROCESSING);
        fundsTransferRepository.save(requestEntity);

        TransactionRequest transactionRequest = mapToRequest(requestEntity, false);

        accountClient.performDebitAndCreditOperations(transactionRequest);

        requestEntity.setStatus(FundsRequestStatus.SUCCESSFUL);
        fundsTransferRepository.save(requestEntity);
    }

    @Transactional
    public void processStalledFundsTransfer() {

        List<FundsTransferEntity> requestEntities = fundsTransferRepository
                .findByStatusAndCreationTimeAfterOrderByCreationTimeAsc(FundsRequestStatus.PROCESSING,
                        Instant.now().minusSeconds(600));

        requestEntities.forEach(this::processStalledRequests);

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processStalledRequests(FundsTransferEntity requestEntity) {

        TransactionRequest transactionRequest = mapToRequest(requestEntity, true);

        accountClient.performDebitAndCreditOperations(transactionRequest);

        requestEntity.setStatus(FundsRequestStatus.SUCCESSFUL);
        fundsTransferRepository.save(requestEntity);
    }

    private TransactionRequest mapToRequest(FundsTransferEntity requestEntity, boolean stalled) {
        TransactionRequest transactionRequest = new TransactionRequest();

        transactionRequest.setAmount(requestEntity.getAmount());
        transactionRequest.setFromOwnerId(requestEntity.getFromOwnerID());
        transactionRequest.setToOwnerId(requestEntity.getToOwnerID());

        if (stalled) {
            try {
                RequestIdentifier identifier = JsonUtil.getMapper().readValue(requestEntity.getRequestIdentifier(), RequestIdentifier.class);
                transactionRequest.setRequestIdentifier(identifier);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

        }
        return transactionRequest;
    }
}
