package com.lime.minipay.repository;

import com.lime.minipay.entity.MainAccount;
import com.lime.minipay.entity.Member;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface MainAccountRepository extends JpaRepository<MainAccount, Long> {
//    @Query(value = "select * from main_account where member_id = :memberId for update", nativeQuery = true)
//    Optional<MainAccount> findByMemberLock(@Param("memberId") Long memberId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select h from MainAccount h where h.member = :member")
    Optional<MainAccount> findByMemberWithLock(Member member);

    @Query("select h from MainAccount h where h.member = :member")
    Optional<MainAccount> findByMember(Member member);
}
