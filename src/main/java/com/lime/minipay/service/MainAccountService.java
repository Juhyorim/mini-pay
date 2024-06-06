package com.lime.minipay.service;

import com.lime.minipay.dto.MainAccountDto.Response;
import com.lime.minipay.entity.MainAccount;
import com.lime.minipay.entity.Member;
import com.lime.minipay.repository.MainAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MainAccountService {
    private final MainAccountRepository mainAccountRepository;

    public Response getMainAccount(Member member) {
        MainAccount account = mainAccountRepository.findByMember(member)
                .orElseThrow(() -> new RuntimeException());

        return Response.builder()
                .balance(account.getBalance())
                .build();
    }
}
