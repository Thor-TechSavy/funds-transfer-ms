package com.quicktransfer.microservice.fundstransfer.client.account;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(value = "account", url = "${app.account.url}")
public interface AccountClient {

    @GetMapping(value = "/v1/account/{ownerId}")
    AccountResponse getAccountDetails(@PathVariable UUID ownerId);

    @PutMapping(value = "/v1/account")
    void updateAccountUpdate(@RequestBody UpdateAccountRequest request);
}
