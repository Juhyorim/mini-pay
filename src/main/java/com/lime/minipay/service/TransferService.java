package com.lime.minipay.service;

import com.lime.minipay.entity.MainAccount;
import com.lime.minipay.entity.Transfer;
import com.lime.minipay.entity.enums.TransferStatus;
import com.lime.minipay.repository.MainAccountRepository;
import com.lime.minipay.repository.TransferRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class TransferService {
    private final TransferRepository transferRepository;
    private final MainAccountRepository mainAccountRepository;

    @Transactional
    public void cancel(Transfer transfer) {
        transfer = transferRepository.findById(transfer.getTransferId())
                .orElseThrow(() -> new RuntimeException());

        //status 바꾸고
        transferRepository.updateStatus(transfer.getTransferId(), TransferStatus.CANCEL);

        //취소 금액 돌려받기
        MainAccount mainAccount = mainAccountRepository.findByIdWithLock(transfer.getFromAccount().getMainAccountId())
                .orElseThrow(() -> new RuntimeException());
        mainAccount.addCash(transfer.getAmount());

        log.info("송금 취소: " + transfer.getAmount() + "원");
    }
}
