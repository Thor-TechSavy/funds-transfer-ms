package com.quicktransfer.fundstransfer;

import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
public class FundsTransferMsApplication {

    public static void main(String[] args) {
        SpringApplication.run(FundsTransferMsApplication.class, args);
    }

}
