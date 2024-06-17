package com.lime.minipay.repository;

import com.lime.minipay.entity.Transfer;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select t from Transfer t where t.transferId = :transferId")
    Optional<Transfer> findByIdWithLock(Long transferId);

}
