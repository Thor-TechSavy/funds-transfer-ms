package com.quicktransfer.fundstransfer.scheduler;

import com.quicktransfer.fundstransfer.service.FundsTransferService;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class FundsProcessorScheduler {

    private final FundsTransferService fundsTransferService;

    public FundsProcessorScheduler(FundsTransferService fundsTransferService) {
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
