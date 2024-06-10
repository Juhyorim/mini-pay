package com.lime.minipay.service;

import com.lime.minipay.dto.MainAccountDto;
import com.lime.minipay.dto.SavingAccountDto;
import com.lime.minipay.dto.SavingAccountDto.ChargeRequest;
import com.lime.minipay.entity.MainAccount;
import com.lime.minipay.entity.Member;
import com.lime.minipay.entity.SavingAccount;
import com.lime.minipay.repository.MainAccountRepository;
import com.lime.minipay.repository.SavingAccountRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Slf4j
@RequiredArgsConstructor
@Service
public class SavingAccountService {
    private final SavingAccountRepository savingAccountRepository;
    private final MainAccountRepository mainAccountRepository;

    public void add(Member member) {
        MainAccount mainAccount = mainAccountRepository.findByMember(member)
                .orElseThrow(() -> new RuntimeException());

        SavingAccount savingAccount = SavingAccount.of(mainAccount);

        savingAccountRepository.save(savingAccount);
    }

    public SavingAccountDto.GetAll find(Member member) {
        List<SavingAccount> savingAccounts = savingAccountRepository.findByMember(member)
                .orElseThrow(() -> new RuntimeException());

        return SavingAccountDto.GetAll.of(savingAccounts);
    }

    public MainAccountDto.Response chargeCash(Member member, ChargeRequest request) {
        SavingAccount savingAccount = savingAccountRepository.findByIdWithLock(request.getSavingAccountId())
                .orElseThrow(() -> new RuntimeException());

        savingAccount.addCash(request.getAmount());

        return MainAccountDto.Response.builder()
                .balance(savingAccount.getBalance())
                .build();
    }
}
