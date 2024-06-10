package com.lime.minipay.repository;

import com.lime.minipay.entity.Member;
import com.lime.minipay.entity.SavingAccount;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SavingAccountRepository extends JpaRepository<SavingAccount, Long> {
    @Query("select sa from SavingAccount sa join sa.mainAccount ma where ma.member = :member")
    Optional<List<SavingAccount>> findByMember(Member member);
}
