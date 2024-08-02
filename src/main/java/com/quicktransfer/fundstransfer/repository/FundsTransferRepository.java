package com.quicktransfer.fundstransfer.repository;

import com.quicktransfer.fundstransfer.entity.FundsTransferEntity;
import com.quicktransfer.fundstransfer.enums.FundsRequestStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FundsTransferRepository extends JpaRepository<FundsTransferEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<FundsTransferEntity> findFirstByStatusOrderByCreationTimeAsc(FundsRequestStatus status);
}