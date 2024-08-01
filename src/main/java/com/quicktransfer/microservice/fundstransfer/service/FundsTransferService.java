package com.quicktransfer.microservice.fundstransfer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quicktransfer.microservice.fundstransfer.client.account.AccountClient;
import com.quicktransfer.microservice.fundstransfer.client.currencyexchange.CurrencyExchangeRateClient;
import com.quicktransfer.microservice.fundstransfer.dto.FundsTransferRequestDto;
import com.quicktransfer.microservice.fundstransfer.entity.FundsTransferEntity;
import com.quicktransfer.microservice.fundstransfer.enums.FundsRequestStatus;
import com.quicktransfer.microservice.fundstransfer.repository.FundsTransferRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
public class FundsTransferService {

    private final AccountClient accountClient;
    private final CurrencyExchangeRateClient currencyExchangeRateClient;
    private final FundsTransferRepository fundsTransferRepository;
    private final

    public FundsTransferService(AccountClient accountClient,
                                CurrencyExchangeRateClient currencyExchangeRateClient,
                                FundsTransferRepository fundsTransferRepository) {
        this.accountClient = accountClient;
        this.currencyExchangeRateClient = currencyExchangeRateClient;
        this.fundsTransferRepository = fundsTransferRepository;
    }


    public FundsTransferEntity createFundsTransferRequest(FundsTransferRequestDto requestDto) {

        ObjectMapper objectMapper = new ObjectMapper();

        String jsonString;
        try {
            jsonString = objectMapper.writeValueAsString(requestDto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        FundsTransferEntity fundsTransferEntity = new FundsTransferEntity();
        fundsTransferEntity.setRequest(jsonString);
        fundsTransferEntity.setStatus(FundsRequestStatus.PENDING);

        return fundsTransferRepository.save(fundsTransferEntity);

    }

    @Transactional
    public void processFundsTransfer() {

        Optional<FundsTransferEntity> entity = fundsTransferRepository
                .findFirstByStatusOrderByCreationTimeAsc(FundsRequestStatus.PENDING);

        if (entity.isPresent()) {

            entity.get().setStatus(FundsRequestStatus.PROCESSING);
            entity.get().setLastUpdateTime(Instant.now());
            fundsTransferRepository.save(entity.get());



            accountClient.getAccountDetails()

        }


    }
}
