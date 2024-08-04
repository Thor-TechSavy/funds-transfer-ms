package com.quicktransfer.fundstransfer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.quicktransfer.fundstransfer.client.AccountClient;
import com.quicktransfer.fundstransfer.client.RequestIdentifier;
import com.quicktransfer.fundstransfer.client.TransactionRequest;
import com.quicktransfer.fundstransfer.client.TransactionResponse;
import com.quicktransfer.fundstransfer.config.FundsTransferProperties;
import com.quicktransfer.fundstransfer.entity.FundsTransferEntity;
import com.quicktransfer.fundstransfer.enums.FundsRequestStatus;
import com.quicktransfer.fundstransfer.exception.FundsTransferException;
import com.quicktransfer.fundstransfer.repository.FundsTransferRepository;
import com.quicktransfer.fundstransfer.util.JsonUtil;
import feign.FeignException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class FundsTransferService {

    public static final String FUND_MS = "FUND-MS";
    private final AccountClient accountClient;
    private final FundsTransferRepository fundsTransferRepository;
    private final FundsTransferProperties properties;

    public FundsTransferService(AccountClient accountClient,
                                FundsTransferRepository fundsTransferRepository, FundsTransferProperties properties) {
        this.accountClient = accountClient;
        this.fundsTransferRepository = fundsTransferRepository;
        this.properties = properties;
    }

    /**
     * Creates a new funds transfer request.
     * <p>
     * This method generates a unique request identifier for the given
     * {@code FundsTransferEntity}, sets the identifier on the entity,
     * and then persists the entity using the {@code fundsTransferRepository}.
     *
     * @param fundsTransferEntity the funds transfer entity containing the details
     *                            of the transfer request to be created.
     * @return the persisted {@code FundsTransferEntity} with the assigned request identifier.
     */
    public FundsTransferEntity createFundsTransferRequest(FundsTransferEntity fundsTransferEntity) {

        String requestIdentifier = createRequestIdentifier(fundsTransferEntity);
        fundsTransferEntity.setRequestIdentifier(requestIdentifier);

        return fundsTransferRepository.save(fundsTransferEntity);

    }

    /**
     * Retrieves a funds transfer request by its UUID.
     * <p>
     * This method looks up a funds transfer request using the given UUID.
     * If the request is found, it is returned. If not, a {@code FundsTransferException}
     * is thrown indicating that the request does not exist.
     *
     * @param uuid the UUID of the funds transfer request to be retrieved.
     * @return the {@code FundsTransferEntity} corresponding to the given UUID.
     * @throws FundsTransferException if no funds transfer request exists for the given UUID.
     */
    public FundsTransferEntity getFundsTransferRequest(UUID uuid) {
        return fundsTransferRepository.findByFundsTransferRequestUUID(uuid)
                .orElseThrow(() -> new FundsTransferException("request doesn't exists for uuid: " + uuid));
    }

    /**
     * Retrieves the top 10 oldest funds transfer requests with the specified status.
     * <p>
     * This method queries the repository to find the first 10 funds transfer requests
     * that have the given status, ordered by their creation time in ascending order.
     *
     * @param status the status of the funds transfer requests to be retrieved.
     * @return a list of the top 10 oldest {@code FundsTransferEntity} with the specified status.
     */
    public List<FundsTransferEntity> getTop10OldestByStatus(FundsRequestStatus status) {
        return fundsTransferRepository.findFirst10ByStatusOrderByCreationTimeAsc(status);
    }

    /**
     * Creates a unique request identifier for the given funds transfer entity.
     * <p>
     * This method constructs a {@code RequestIdentifier} object using the details from
     * the provided {@code FundsTransferEntity} and serializes it to a JSON string.
     *
     * @param fundsTransferEntity the funds transfer entity for which the request identifier is being created.
     * @return a JSON string representing the unique request identifier.
     * @throws FundsTransferException if there is an error during the serialization process.
     */
    private static String createRequestIdentifier(FundsTransferEntity fundsTransferEntity) {
        RequestIdentifier identifier = new RequestIdentifier();
        identifier.setCalleeName(FUND_MS);
        identifier.setRequestTime(String.valueOf(fundsTransferEntity.getCreationTime()));
        identifier.setTransferRequestId(fundsTransferEntity.getFundsTransferRequestUUID());

        try {
            return JsonUtil.getMapper().writeValueAsString(identifier);
        } catch (JsonProcessingException e) {
            throw new FundsTransferException(e.getMessage());
        }
    }

    /**
     * Retrieves a list of funds transfer entities that have been in the {@code PROCESSING} status
     * and were created within the last {@code N} minutes.
     * <p>
     * This method queries the {@code fundsTransferRepository} to find all funds transfer entities
     * with the status {@code PROCESSING} that were created after a timestamp representing {@code N} minutes ago.
     * The entities are returned in ascending order of their creation time.
     * <p>
     * The number of minutes {@code N} is derived from the application properties, which are parsed to seconds.
     *
     * @return a list of {@code FundsTransferEntity} objects that are currently in the {@code PROCESSING} status
     * and were created within the last {@code N} minutes.
     */
    public List<FundsTransferEntity> getFundsTransferEntitiesForLastNMinutes() {
        return fundsTransferRepository
                .findByStatusAndCreationTimeAfterOrderByCreationTimeAsc(FundsRequestStatus.PROCESSING,
                        Instant.now().minusSeconds(Long.parseLong(properties.getFetchLastSeconds())));
    }

    /**
     * Updates the provided {@code FundsTransferEntity} by setting its last update time to the current timestamp
     * and then saving the updated entity to the repository.
     * <p>
     * This method modifies the {@code lastUpdateTime} field of the given {@code FundsTransferEntity} to the current
     * time and persists these changes using the {@code fundsTransferRepository}.
     *
     * @param entity the {@code FundsTransferEntity} to be updated. It must be an existing entity with a valid ID.
     *               The {@code lastUpdateTime} field of this entity will be updated to the current time.
     */
    public void update(FundsTransferEntity entity) {
        entity.setLastUpdateTime(Instant.now());
        fundsTransferRepository.save(entity);
    }

    /**
     * Processes the provided funds transfer request by performing debit and credit operations through an external service.
     * The status of the request is updated based on the response from the external service or in case of an exception.
     * <p>
     * This method performs the following steps:
     * 1. Maps the {@code FundsTransferEntity} to a {@code TransactionRequest}.
     * 2. Sets the status of the request entity to {@code FundsRequestStatus.PROCESSING}.
     * 3. Calls the external account service to perform debit and credit operations.
     * 4. Updates the status of the request entity based on the response or sets it to {@code FundsRequestStatus.FAILED} in case of exceptions.
     * 5. Updates the request entity in the repository, regardless of success or failure.
     *
     * @param requestEntity the {@code FundsTransferEntity} to be processed. This entity should have a valid ID and initial status.
     */
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
        } finally {
            update(requestEntity);
        }

    }


    /**
     * Maps a {@code FundsTransferEntity} to a {@code TransactionRequest}.
     * <p>
     * This method sets the amount, sender, and receiver IDs from the {@code FundsTransferEntity} to the {@code TransactionRequest}.
     * It also converts the {@code requestIdentifier} from JSON format to a {@code RequestIdentifier} object.
     *
     * @param requestEntity the {@code FundsTransferEntity} to be mapped. This entity should have valid fields for amount, owner IDs, and request identifier.
     * @return a {@code TransactionRequest} with values mapped from the provided {@code FundsTransferEntity}.
     * @throws FundsTransferException if there is an error processing the JSON request identifier.
     */
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
