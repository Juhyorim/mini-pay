package com.lime.minipay.service;

import com.lime.minipay.dto.MainAccountDto;
import com.lime.minipay.dto.MainAccountDto.AddCashRequest;
import com.lime.minipay.dto.TransferDto;
import com.lime.minipay.entity.MainAccount;
import com.lime.minipay.entity.Member;
import com.lime.minipay.entity.Transfer;
import com.lime.minipay.repository.MainAccountRepository;
import com.lime.minipay.repository.MemberRepository;
import com.lime.minipay.repository.TransferRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional(isolation = Isolation.REPEATABLE_READ)
@RequiredArgsConstructor
@Service
public class MainAccountService {
    private final MainAccountRepository mainAccountRepository;
    private final MemberRepository memberRepository;
    private final TransferRepository transferRepository;

    public MainAccountDto.Response getMainAccount(Member member) {
        MainAccount account = mainAccountRepository.findByMemberWithLock(member)
                .orElseThrow(() -> new RuntimeException());

        return MainAccountDto.Response.builder()
                .balance(account.getBalance())
                .build();
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    public MainAccountDto.Response addCash(Member member, AddCashRequest request) throws InterruptedException {
        MainAccount mainAccount = mainAccountRepository.findByMemberWithLock(member)
                .orElseThrow(() -> new RuntimeException());

        log.info("###: " + mainAccount.getBalance());
        mainAccount.addCash(request.getAmount());
        log.info("###: " + mainAccount.getBalance());

        return MainAccountDto.Response.builder()
                .balance(mainAccount.getBalance())
                .build();
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void initDayCharged() {
        mainAccountRepository.initDayCharged();
    }

    public TransferDto.TransferResponse transferToMember(Member member,
                                                         MainAccountDto.TransferToMemberRequest request) {
        Member toUser = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new RuntimeException());
        log.info("송금 생성: " + member.getMemberId() + " => " + toUser.getMemberId());

        MainAccount toAccount = mainAccountRepository.findByMemberWithLock(toUser)
                .orElseThrow(() -> new RuntimeException());

        //TODO 데드락 발생가능 확인
        MainAccount fromAccount = mainAccountRepository.findByMemberWithLock(member)
                .orElseThrow(() -> new RuntimeException());

        //송금 생성
        Transfer transfer = Transfer.of(fromAccount, toAccount, request.getAmount());
        transferRepository.save(transfer);

        return TransferDto.TransferResponse.builder()
                .transferId(transfer.getTransferId())
                .remain(fromAccount.getBalance())
                .build();
    }
}
