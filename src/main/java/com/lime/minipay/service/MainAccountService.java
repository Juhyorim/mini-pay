package com.lime.minipay.service;

import com.lime.minipay.dto.MainAccountDto;
import com.lime.minipay.dto.MainAccountDto.AddCashRequest;
import com.lime.minipay.dto.TransferDto;
import com.lime.minipay.dto.TransferDto.Info;
import com.lime.minipay.entity.MainAccount;
import com.lime.minipay.entity.Member;
import com.lime.minipay.entity.Transfer;
import com.lime.minipay.error.ForbiddenException;
import com.lime.minipay.repository.MainAccountRepository;
import com.lime.minipay.repository.MemberRepository;
import com.lime.minipay.repository.TransferRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Slf4j
@RequiredArgsConstructor
@Service
public class MainAccountService {
    private final TaskScheduler taskScheduler;
    private final MainAccountRepository mainAccountRepository;
    private final MemberRepository memberRepository;
    private final TransferRepository transferRepository;
    private final TransferService transferService;

    private final PlatformTransactionManager txManager;

    public MainAccountDto.Response getMainAccount(Member member) {
        MainAccount account = mainAccountRepository.findByMemberWithLock(member)
                .orElseThrow(() -> new RuntimeException());

        return MainAccountDto.Response.builder()
                .balance(account.getBalance())
                .build();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ)
    public MainAccountDto.Response addCash(Member member, AddCashRequest request) {
        MainAccount mainAccount = mainAccountRepository.findByMemberWithLock(member)
                .orElseThrow(() -> new RuntimeException());

        log.info("###: " + mainAccount.getBalance());
        mainAccount.addCash(request.getAmount());
        log.info("###: " + mainAccount.getBalance());

        return MainAccountDto.Response.builder()
                .balance(mainAccount.getBalance())
                .build();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ)
    public MainAccountDto.Response addCashToAccount(MainAccount mainAccount, AddCashRequest request) {
        mainAccountRepository.findByIdWithLock(mainAccount.getMainAccountId())
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

    @Transactional
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

        //24시간 후 리마인딩 스케줄 추가
        Runnable remindTask = () -> log.info("송금 리마인딩");
        long remindDelay = 24 * 60 * 60 * 1000L;
        taskScheduler.schedule(remindTask, new java.util.Date(System.currentTimeMillis() + remindDelay));

        //72시간 후 취소 스케줄 추가
        Runnable task = () -> transferService.cancel(transfer);
//        long transferCancelDelay = 1 * 10 * 1000L; //10초컷 - 테스트용
        long transferCancelDelay = 72 * 60 * 60 * 1000L; //실제로직
        taskScheduler.schedule(task, new java.util.Date(System.currentTimeMillis() + transferCancelDelay));

        return TransferDto.TransferResponse.builder()
                .transferId(transfer.getTransferId())
                .remain(fromAccount.getBalance())
                .build();
    }

    public TransferDto.TransferResponse transferToMemberWithTxManager(Member member,
                                                         MainAccountDto.TransferToMemberRequest request) {
        Member toUser = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new RuntimeException());
        log.info("송금 생성: " + member.getMemberId() + " => " + toUser.getMemberId());

        TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());

        MainAccount fromAccount = null;
        Transfer transfer = null;
        try {
            MainAccount toAccount = mainAccountRepository.findByMemberWithLock(toUser)
                    .orElseThrow(() -> new RuntimeException());

            //TODO 데드락 발생가능 확인
            fromAccount = mainAccountRepository.findByMemberWithLock(member)
                    .orElseThrow(() -> new RuntimeException());

            //송금 생성
            transfer = Transfer.of(fromAccount, toAccount, request.getAmount());
            transferRepository.save(transfer);
            txManager.rollback(status);
            //tx 끝
        } catch (RuntimeException ex) {
            txManager.rollback(status);
            throw ex;
        }

        //24시간 후 리마인딩 스케줄 추가
        Runnable remindTask = () -> log.info("송금 리마인딩");
        long remindDelay = 24 * 60 * 60 * 1000L;
        taskScheduler.schedule(remindTask, new java.util.Date(System.currentTimeMillis() + remindDelay));

        //72시간 후 취소 스케줄 추가
        if (transfer != null) {
            Transfer finalTransfer = transfer;
            Runnable task = () -> transferService.cancel(finalTransfer);
//        long transferCancelDelay = 1 * 10 * 1000L; //10초컷 - 테스트용
            long transferCancelDelay = 72 * 60 * 60 * 1000L; //실제로직
            taskScheduler.schedule(task, new java.util.Date(System.currentTimeMillis() + transferCancelDelay));
        }

        return TransferDto.TransferResponse.builder()
                .transferId(transfer.getTransferId())
                .remain(fromAccount.getBalance())
                .build();
    }

    public TransferDto.Info getTransferDetail(Member member, Long transferId) {
        Transfer transfer = transferRepository.findById(transferId)
                .orElseThrow(() -> new RuntimeException("찾을 수 없습니다"));

        return TransferDto.Info.builder()
                .transferId(transferId)
                .toMemberId(transfer.getToAccount().getMember().getMemberId())
                .toMemberName(transfer.getToAccount().getMember().getName())
                .fromMemberId(transfer.getFromAccount().getMember().getMemberId())
                .fromMemberName(transfer.getFromAccount().getMember().getName())
                .amount(transfer.getAmount())
                .createdAt(transfer.getCreatedAt())
                .status(transfer.getStatus())
                .build();
    }

    public Info cancel(Member member, Long transferId) {
        Transfer transfer = transferRepository.findByIdWithLock(transferId)
                .orElseThrow(() -> new RuntimeException("찾을 수 없습니다"));

        //보낸 사람만 취소 가능
        if (!member.equals(transfer.getFromAccount().getMember())) {
            throw new ForbiddenException("권한이 없습니다");
        }

        MainAccount mainAccount = mainAccountRepository.findByMemberWithLock(member)
                .orElseThrow(() -> new RuntimeException());

        transfer.cancel();

        return TransferDto.Info.of(transfer);
    }

    public Info approve(Member member, Long transferId) {
        Transfer transfer = transferRepository.findByIdWithLock(transferId)
                .orElseThrow(() -> new RuntimeException("찾을 수 없습니다"));

        //받은 사람만 승인 가능
        if (!member.equals(transfer.getToAccount().getMember())) {
            throw new ForbiddenException("권한이 없습니다");
        }

        //내 계좌 락
        MainAccount mainAccount = mainAccountRepository.findByMemberWithLock(member)
                .orElseThrow(() -> new RuntimeException());

        transfer.approve();

        return TransferDto.Info.of(transfer);
    }
}
