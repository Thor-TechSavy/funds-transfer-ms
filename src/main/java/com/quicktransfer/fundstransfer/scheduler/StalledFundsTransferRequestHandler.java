package com.quicktransfer.fundstransfer.scheduler;

import com.quicktransfer.fundstransfer.service.FundsTransferService;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class StalledFundsTransferRequestHandler {

    private final FundsTransferService fundsTransferService;

    public StalledFundsTransferRequestHandler(FundsTransferService fundsTransferService) {
        this.fundsTransferService = fundsTransferService;
    }

    @Transactional
    @Scheduled(cron = "0 0/10 * * * ?")
    @SchedulerLock(name = "fundsProcessor_scheduledTask",
            lockAtMostFor = "PT2M")
    public void scheduledTask() {
        fundsTransferService.processStalledFundsTransfer();

    }
}
