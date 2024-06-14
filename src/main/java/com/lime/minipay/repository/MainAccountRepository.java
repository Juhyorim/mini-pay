package com.lime.minipay.repository;

import com.lime.minipay.entity.MainAccount;
import com.lime.minipay.entity.Member;
import jakarta.persistence.LockModeType;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MainAccountRepository extends JpaRepository<MainAccount, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select h from MainAccount h where h.member = :member")
    Optional<MainAccount> findByMemberWithLock(Member member);

    @Query("select h from MainAccount h where h.member = :member")
    Optional<MainAccount> findByMember(Member member);

    @Modifying
    @Query("update MainAccount m set m.dayCharged = 0")
    void initDayCharged();
}
