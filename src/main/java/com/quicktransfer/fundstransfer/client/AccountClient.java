package com.quicktransfer.fundstransfer.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "account", url = "${app.account.url}")
public interface AccountClient {

    @PostMapping("/transaction")
    TransactionResponse performDebitAndCreditOperations(@RequestBody TransactionRequest transactionDto);

}
