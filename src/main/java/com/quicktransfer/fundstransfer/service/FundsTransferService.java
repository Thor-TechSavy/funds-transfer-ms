package com.quicktransfer.fundstransfer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.quicktransfer.fundstransfer.client.AccountClient;
import com.quicktransfer.fundstransfer.client.RequestIdentifier;
import com.quicktransfer.fundstransfer.client.TransactionRequest;
import com.quicktransfer.fundstransfer.client.TransactionResponse;
import com.quicktransfer.fundstransfer.dto.FundsTransferRequestDto;
import com.quicktransfer.fundstransfer.entity.FundsTransferEntity;
import com.quicktransfer.fundstransfer.enums.FundsRequestStatus;
import com.quicktransfer.fundstransfer.exception.FundsTransferException;
import com.quicktransfer.fundstransfer.repository.FundsTransferRepository;
import com.quicktransfer.fundstransfer.util.JsonUtil;
import feign.FeignException;
import org.apache.coyote.BadRequestException;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

import static org.bouncycastle.asn1.x500.style.RFC4519Style.c;

@Service
public class FundsTransferService {

    private final AccountClient accountClient;
    private final FundsTransferRepository fundsTransferRepository;

    public FundsTransferService(AccountClient accountClient,
                                FundsTransferRepository fundsTransferRepository) {
        this.accountClient = accountClient;
        this.fundsTransferRepository = fundsTransferRepository;
    }


    public FundsTransferEntity createFundsTransferRequest(FundsTransferEntity fundsTransferEntity) {

        String requestIdentifier = createRequestIdentifier(fundsTransferEntity);
        fundsTransferEntity.setRequestIdentifier(requestIdentifier);

        return fundsTransferRepository.save(fundsTransferEntity);

    }

    public List<FundsTransferEntity> findFirst10ByStatusOrderByCreationTimeAsc(FundsRequestStatus status) {
        return fundsTransferRepository.findFirst10ByStatusOrderByCreationTimeAsc(status);
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

    public List<FundsTransferEntity> getFundsTransferEntitiesForLastNMinutes() {
        return fundsTransferRepository
                .findByStatusAndCreationTimeAfterOrderByCreationTimeAsc(FundsRequestStatus.PROCESSING,
                        Instant.now().minusSeconds(600));
    }

    public FundsTransferEntity update(FundsTransferEntity entity) {
        entity.setLastUpdateTime(Instant.now());
        return fundsTransferRepository.save(entity);
    }

    @Transactional
    public void processTransferRequest(FundsTransferEntity requestEntity) {

        TransactionRequest transactionRequest = mapToRequest(requestEntity);
        requestEntity.setStatus(FundsRequestStatus.PROCESSING);
        try {
            TransactionResponse response = accountClient.performDebitAndCreditOperations(transactionRequest);

            FundsRequestStatus newStatus = response.getTransactionStatus();

            requestEntity.setStatus(newStatus);

        } catch (FeignException e) {
            //
        } catch (FundsTransferException e) {
            requestEntity.setStatus(FundsRequestStatus.FAILED);
        }
        finally {
            update(requestEntity);
        }

    }


    private TransactionRequest mapToRequest(FundsTransferEntity requestEntity) {
        TransactionRequest transactionRequest = new TransactionRequest();

        transactionRequest.setAmount(requestEntity.getAmount());
        transactionRequest.setFromOwnerId(requestEntity.getFromOwnerID());
        transactionRequest.setToOwnerId(requestEntity.getToOwnerID());

        try {
            RequestIdentifier identifier = JsonUtil.getMapper().readValue(requestEntity.getRequestIdentifier(), RequestIdentifier.class);
            transactionRequest.setRequestIdentifier(identifier);
        } catch (JsonProcessingException e) {
            throw new FundsTransferException(e.getMessage());
        }


        return transactionRequest;
    }
}
