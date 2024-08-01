package com.quicktransfer.microservice.fundstransfer.client.currencyexchange;

import com.quicktransfer.microservice.fundstransfer.client.account.AccountResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.UUID;

@FeignClient(value = "currencyExchange", url = "${app.currencyExchange.url}")
public interface CurrencyExchangeRateClient {

    @GetMapping(value = "/v1/account/{ownerId}")
    BigDecimal getAccountDetails();


}
