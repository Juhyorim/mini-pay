package com.lime.minipay.repository;

import com.lime.minipay.entity.Member;
import com.lime.minipay.entity.SavingAccount;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface SavingAccountRepository extends JpaRepository<SavingAccount, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select sa from SavingAccount sa join sa.mainAccount ma where ma.member = :member")
    Optional<List<SavingAccount>> findByMember(Member member);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select sa from SavingAccount sa where sa.savingAccountId=:id")
    Optional<SavingAccount> findByIdWithLock(Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select sa from SavingAccount sa")
    Optional<List<SavingAccount>> findAllWithLock();
}
