package com.lime.minipay.service;

import com.lime.minipay.dto.MainAccountDto;
import com.lime.minipay.dto.SavingAccountDto;
import com.lime.minipay.dto.SavingAccountDto.ChargeRequest;
import com.lime.minipay.entity.MainAccount;
import com.lime.minipay.entity.Member;
import com.lime.minipay.entity.SavingAccount;
import com.lime.minipay.error.ForbiddenException;
import com.lime.minipay.error.NoMoneyEnoughException;
import com.lime.minipay.repository.MainAccountRepository;
import com.lime.minipay.repository.SavingAccountRepository;
import com.lime.minipay.strategy.saving.SavingStrategy;
import java.util.List;
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
    private final List<SavingStrategy> savingStrategies;
    private final MainAccountService mainAccountService;

    public void add(Member member, SavingAccountDto.AddAccountRequest request) {
        MainAccount mainAccount = mainAccountRepository.findByMemberWithLock(member)
                .orElseThrow(() -> new RuntimeException());

        SavingAccount savingAccount = SavingAccount.of(mainAccount, request.getType());

        savingAccountRepository.save(savingAccount);
    }

    public SavingAccountDto.GetAll find(Member member) {
        List<SavingAccount> savingAccounts = savingAccountRepository.findByMember(member)
                .orElseThrow(() -> new RuntimeException());

        return SavingAccountDto.GetAll.of(savingAccounts);
    }

    public MainAccountDto.Response chargeCash(Member member, ChargeRequest request) throws InterruptedException {
        MainAccount memberMainAccount = mainAccountRepository.findByMember(member)
                .orElseThrow(() -> new RuntimeException());

        SavingAccount savingAccount = savingAccountRepository.findByIdWithLock(request.getSavingAccountId())
                .orElseThrow(() -> new RuntimeException());

        if (!savingAccount.getMainAccount().getMainAccountId().equals(memberMainAccount.getMainAccountId())) {
            throw new ForbiddenException("엑세스 권한이 없습니다");
        }

        try {
            savingAccount.addCash(request.getAmount());
        } catch (NoMoneyEnoughException e) {
            mainAccountService.addCash(member, new MainAccountDto.AddCashRequest(request.getAmount()));
            memberMainAccount = mainAccountRepository.findByMember(member)
                    .orElseThrow(() -> new RuntimeException());

            savingAccount.addCash(request.getAmount());
        }

        return MainAccountDto.Response.builder()
                .balance(savingAccount.getBalance())
                .build();
    }

    @Transactional
    public void addInterestForAllMember() {
        List<SavingAccount> savingAccounts = savingAccountRepository.findAllWithLock()
                .orElseThrow(() -> new RuntimeException());

        //TODO exception 발생 시 재시도 로직 생각
        for (SavingAccount savingAccount : savingAccounts) {
            Long interest = getInterest(savingAccount, savingAccount.getPrincipal());
            savingAccount.addInterest(interest);
            log.info("변경 완료: " + interest);
        }
    }

    private Long getInterest(SavingAccount savingAccount, Long principal) {
        return savingStrategies.stream()
                .filter(savingAccountStrategy -> savingAccountStrategy.isTarget(savingAccount.getType()))
                .findAny()
                .orElseThrow(() -> new RuntimeException("허용하지 않는 적금계좌 유형입니다"))
                .calculateInterest(principal);
    }
}
