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

    /**
     * Scheduled task for handling pending funds transfer requests.
     * <p>
     * This method retrieves the top 10 oldest funds transfer requests that have a status of {@link FundsRequestStatus#PENDING}.
     * It then processes each request. The method is annotated with {@link Scheduled} to run periodically according to the
     * cron expression specified in the application's properties. Additionally, it is annotated with {@link Transactional}
     * to ensure that all database operations within this method are executed within a single transaction.
     * </p>
     * <p>
     * The method is also protected by a {@link SchedulerLock} to prevent multiple instances of the scheduled task from
     * running concurrently. The lock ensures that this method is not executed by multiple instances of the application
     * simultaneously, with a minimum lock duration of 5 minutes and a maximum of 15 minutes.
     * </p>
     * <p>
     * If no pending requests are found, the method logs this information and exits. If exceptions occur during processing,
     * they are logged to help with troubleshooting.
     * </p>
     */
    @Transactional
    @Scheduled(cron = "${fundstransfer.pending-requests-scheduler}")
    @SchedulerLock(name = "fundsProcessor_scheduledTask",
            lockAtLeastFor = "PT5M", lockAtMostFor = "PT15M")
    public void handlePendingFundsTransferRequests() {

        List<FundsTransferEntity> requestEntities = fundsTransferService
                .getTop10OldestByStatus(FundsRequestStatus.PENDING);

        if (requestEntities.isEmpty()) {
            return;
        }

        requestEntities.forEach(fundsTransferService::processTransferRequest);

    }

    /**
     * Scheduled task for handling stalled funds transfer requests.
     * <p>
     * This method retrieves funds transfer requests that have been stalled for a specified duration,
     * as configured in the application's properties. The method is annotated with {@link Scheduled}
     * to run periodically according to the cron expression specified in the application's properties.
     * It also ensures that the task is executed within a single transaction using the {@link Transactional}
     * annotation.
     * </p>
     * <p>
     * The method is protected by a {@link SchedulerLock} to prevent multiple instances of the scheduled
     * task from running concurrently. The lock ensures that this method is not executed by multiple
     * instances of the application simultaneously, with a maximum lock duration of 10 minutes.
     * </p>
     * <p>
     * If no stalled requests are found, the method simply returns without further action. If stalled
     * requests are found, each request is processed by invoking {@link FundsTransferService#processTransferRequest(FundsTransferEntity)}.
     * </p>
     */

    @Transactional
    @Scheduled(cron = "${fundstransfer.stalled-requests-scheduler}")
    @SchedulerLock(name = "stalledFundsRequestProcessor_scheduledTask",
            lockAtMostFor = "PT10M")
    public void handleStalledFundsTransferRequests() {

        List<FundsTransferEntity> requestEntities = fundsTransferService.getFundsTransferEntitiesForLastNMinutes();

        if (requestEntities.isEmpty()) {
            return;
        }

        requestEntities.forEach(fundsTransferService::processTransferRequest);

    }
}
