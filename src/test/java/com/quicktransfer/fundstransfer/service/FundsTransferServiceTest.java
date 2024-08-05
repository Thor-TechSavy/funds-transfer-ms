package com.quicktransfer.fundstransfer.service;

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
import feign.Request;
import feign.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FundsTransferServiceTest {

    @Mock
    private AccountClient accountClient;

    @Mock
    private FundsTransferRepository fundsTransferRepository;

    @Mock
    private FundsTransferProperties properties;

    @InjectMocks
    private FundsTransferService fundsTransferService;

    @Captor
    private ArgumentCaptor<FundsTransferEntity> entityCaptor;

    private FundsTransferEntity fundsTransferEntity;
    private static final String REQUEST_IDENTIFIER_JSON = "{\"calleeName\":\"FUND-MS\",\"requestTime\":\"test-time\",\"transferRequestId\":\"775bbf93-8082-439e-89d5-e585224de199\"}";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        fundsTransferEntity = new FundsTransferEntity();
        fundsTransferEntity.setFromOwnerID(UUID.randomUUID());
        fundsTransferEntity.setToOwnerID(UUID.randomUUID());
        fundsTransferEntity.setAmount(new BigDecimal("1000"));
        fundsTransferEntity.setStatus(FundsRequestStatus.PENDING);
    }

    @AfterEach
    void tearDown() {
        reset(
                accountClient,
                fundsTransferRepository,
                properties
        );
    }

    @Test
    void testCreateFundsTransferRequest() {
        when(fundsTransferRepository.save(any(FundsTransferEntity.class))).thenReturn(fundsTransferEntity);

        var createdEntity = fundsTransferService.createFundsTransferRequest(fundsTransferEntity);

        assertNotNull(createdEntity);
        assertEquals(FundsRequestStatus.PENDING, createdEntity.getStatus());
        assertNotNull(createdEntity.getRequestIdentifier());
        verify(fundsTransferRepository, times(1)).save(fundsTransferEntity);
    }

    @Test
    void testGetFundsTransferRequest() {
        var uuid = UUID.randomUUID();
        when(fundsTransferRepository.findByFundsTransferRequestUUID(uuid)).thenReturn(Optional.of(fundsTransferEntity));

        var result = fundsTransferService.getFundsTransferRequest(uuid);

        assertNotNull(result);
        assertEquals(fundsTransferEntity, result);
    }

    @Test
    void testGetFundsTransferRequestNotFound() {
        var uuid = UUID.randomUUID();
        when(fundsTransferRepository.findByFundsTransferRequestUUID(uuid)).thenReturn(Optional.empty());

        var exception = assertThrows(FundsTransferException.class, () -> {
            fundsTransferService.getFundsTransferRequest(uuid);
        });

        assertEquals("request doesn't exists for uuid: " + uuid, exception.getMessage());
    }

    @Test
    void testGetTop10OldestByStatus() {
        when(fundsTransferRepository.findFirst10ByStatusOrderByCreationTimeAsc(FundsRequestStatus.PENDING))
                .thenReturn(List.of(fundsTransferEntity));

        var result = fundsTransferService.getTop10OldestByStatus(FundsRequestStatus.PENDING);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(fundsTransferEntity, result.get(0));
    }

    @Test
    void testCreateRequestIdentifier() {
        when(fundsTransferRepository.save(any(FundsTransferEntity.class))).thenReturn(fundsTransferEntity);
        fundsTransferEntity.setRequestIdentifier(null);

        var identifierJson = fundsTransferService.createFundsTransferRequest(fundsTransferEntity).getRequestIdentifier();

        assertNotNull(identifierJson);
        assertDoesNotThrow(() -> {
            JsonUtil.getMapper().readValue(identifierJson, RequestIdentifier.class);
        });
    }

    @Test
    void testGetFundsTransferEntitiesForLastNMinutes() {
        when(properties.getFetchLastSeconds()).thenReturn("600");
        when(fundsTransferRepository.findByStatusAndCreationTimeAfterOrderByCreationTimeAsc(
                eq(FundsRequestStatus.PROCESSING), any(Instant.class)))
                .thenReturn(List.of(fundsTransferEntity));

        var result = fundsTransferService.getFundsTransferEntitiesForLastNMinutes();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(fundsTransferEntity, result.get(0));
    }

    @Test
    void testUpdate() {
        fundsTransferService.update(fundsTransferEntity);

        verify(fundsTransferRepository, times(1)).save(entityCaptor.capture());
        assertNotNull(entityCaptor.getValue().getLastUpdateTime());
    }

    @Test
    void testProcessTransferRequest() {
        fundsTransferEntity.setRequestIdentifier(REQUEST_IDENTIFIER_JSON);
        var response = new TransactionResponse();
        response.setAmount(BigDecimal.valueOf(90.0));
        response.setFromOwnerId(UUID.randomUUID());
        response.setToOwnerId(UUID.randomUUID());
        response.setTransactionId(UUID.randomUUID());
        response.setTransactionStatus(FundsRequestStatus.SUCCESSFUL);
        when(accountClient.performDebitAndCreditOperations(any(TransactionRequest.class))).thenReturn(response);

        fundsTransferService.processTransferRequest(fundsTransferEntity);

        verify(fundsTransferRepository, times(1)).save(fundsTransferEntity);
        assertEquals(FundsRequestStatus.SUCCESSFUL, fundsTransferEntity.getStatus());
    }

    @Test
    void testProcessTransferRequestFeignException() {
        fundsTransferEntity.setRequestIdentifier(REQUEST_IDENTIFIER_JSON);

        // Create a mock FeignException with a response
        Request request = Request.create(Request.HttpMethod.POST, "url", Map.of(), new byte[]{}, StandardCharsets.UTF_8, null);
        Response response = Response.builder()
                .status(400)
                .reason("Bad Request")
                .request(request)
                .body("Bad Request", StandardCharsets.UTF_8)
                .build();
        FeignException feignException = new FeignException.BadRequest("Bad Request", request, new byte[0], Map.of("key", List.of("")));

        when(accountClient.performDebitAndCreditOperations(any(TransactionRequest.class))).thenThrow(feignException);

        fundsTransferService.processTransferRequest(fundsTransferEntity);

        verify(fundsTransferRepository, times(1)).save(fundsTransferEntity);
        // won't change the status to FAILED because of revisiting it later
        assertEquals(FundsRequestStatus.PROCESSING, fundsTransferEntity.getStatus());
    }

    @Test
    void testProcessTransferRequestFundsTransferRequestException() {
        fundsTransferEntity.setRequestIdentifier(REQUEST_IDENTIFIER_JSON);

        when(accountClient.performDebitAndCreditOperations(any(TransactionRequest.class))).thenThrow(new FundsTransferException("failed"));

        fundsTransferService.processTransferRequest(fundsTransferEntity);

        verify(fundsTransferRepository, times(1)).save(fundsTransferEntity);
        assertEquals(FundsRequestStatus.FAILED, fundsTransferEntity.getStatus());
    }
}
