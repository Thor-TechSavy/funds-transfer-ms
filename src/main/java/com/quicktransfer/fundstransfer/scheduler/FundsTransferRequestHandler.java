package com.quicktransfer.fundstransfer.scheduler;

import com.quicktransfer.fundstransfer.entity.FundsTransferEntity;
import com.quicktransfer.fundstransfer.enums.FundsRequestStatus;
import com.quicktransfer.fundstransfer.service.FundsTransferService;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class FundsTransferRequestHandler {

    private final FundsTransferService fundsTransferService;

    public FundsTransferRequestHandler(FundsTransferService fundsTransferService) {
        this.fundsTransferService = fundsTransferService;
    }


    @Transactional
    @Scheduled(cron = "0 0/15 * * * ?")
    @SchedulerLock(name = "fundsProcessor_scheduledTask",
            lockAtLeastFor = "PT5M", lockAtMostFor = "PT14M")
    public void scheduledTask() {

        fundsTransferService.processFundsTransfer();
    }
}
