package com.quicktransfer.fundstransfer.repository;

import com.quicktransfer.fundstransfer.entity.FundsTransferEntity;
import com.quicktransfer.fundstransfer.enums.FundsRequestStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class FundsTransferRepositoryTest {

    @Autowired
    private FundsTransferRepository fundsTransferRepository;

    private FundsTransferEntity fundsTransferEntity;

    @BeforeEach
    void setUp() {
        fundsTransferEntity = new FundsTransferEntity();
        // Fields set up
        fundsTransferEntity.setFromOwnerID(UUID.randomUUID());
        fundsTransferEntity.setToOwnerID(UUID.randomUUID());
        fundsTransferEntity.setAmount(new BigDecimal("1000"));
        fundsTransferEntity.setRequestIdentifier("request-identifier");
        fundsTransferEntity.setStatus(FundsRequestStatus.PENDING);
        // Save entity
        fundsTransferRepository.save(fundsTransferEntity);
    }

    @Test
    void testFindFirst10ByStatusOrderByCreationTimeAsc() {
        var entities = fundsTransferRepository
                .findFirst10ByStatusOrderByCreationTimeAsc(FundsRequestStatus.PENDING);

        assertNotNull(entities);
        assertFalse(entities.isEmpty());
        assertEquals(1, entities.size());
        assertEquals(fundsTransferEntity.getFundsTransferRequestUUID(), entities.get(0).getFundsTransferRequestUUID());
    }

    @Test
    void testFindByStatusAndCreationTimeAfterOrderByCreationTimeAsc() {
        var entities = fundsTransferRepository
                .findByStatusAndCreationTimeAfterOrderByCreationTimeAsc(FundsRequestStatus.PENDING, Instant.now().minusSeconds(60));

        assertNotNull(entities);
        assertFalse(entities.isEmpty());
        assertEquals(1, entities.size());
        assertEquals(fundsTransferEntity.getFundsTransferRequestUUID(), entities.get(0).getFundsTransferRequestUUID());
    }

    @Test
    void testFindByFundsTransferRequestUUID() {
        var entity = fundsTransferRepository
                .findByFundsTransferRequestUUID(fundsTransferEntity.getFundsTransferRequestUUID());

        assertTrue(entity.isPresent());
        assertEquals(fundsTransferEntity.getFundsTransferRequestUUID(), entity.get().getFundsTransferRequestUUID());
    }
}