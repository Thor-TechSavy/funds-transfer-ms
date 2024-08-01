package com.quicktransfer.microservice.fundstransfer.repository;

import com.quicktransfer.microservice.fundstransfer.entity.FundsTransferEntity;
import com.quicktransfer.microservice.fundstransfer.enums.FundsRequestStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FundsTransferRepository extends JpaRepository<FundsTransferEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<FundsTransferEntity> findFirstByStatusOrderByCreationTimeAsc(FundsRequestStatus status);
}