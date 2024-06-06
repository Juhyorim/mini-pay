package com.lime.minipay.repository;

import com.lime.minipay.entity.MainAccount;
import com.lime.minipay.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MainAccountRepository extends JpaRepository<MainAccount, Long> {
    Optional<MainAccount> findByMember(Member member);
}
