package com.lime.minipay.repository;

import com.lime.minipay.entity.MemberSettlement;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface MemberSettlementRepository extends JpaRepository<MemberSettlement, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select ms from MemberSettlement ms where ms.memberSettlementId = :memberSettlementId")
    Optional<MemberSettlement> findByIdWithLock(Long memberSettlementId);
}
