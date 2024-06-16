package com.lime.minipay.repository;

import com.lime.minipay.entity.MemberSettlement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberSettlementRepository extends JpaRepository<MemberSettlement, Long> {
}
