package com.lime.minipay;

import com.lime.minipay.dto.MainAccountDto.AddCashRequest;
import com.lime.minipay.entity.Member;
import com.lime.minipay.repository.MemberRepository;
import com.lime.minipay.service.MainAccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AccountServiceTest {

    @Autowired
    private MainAccountService accountService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void testAddCashConcurrency() throws InterruptedException {
        Member member = memberRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException());

        Runnable task = () -> {
            try {
                AddCashRequest request = new AddCashRequest(100L);
                accountService.addCash(member, request);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        Thread thread1 = new Thread(task);
        Thread thread2 = new Thread(task);

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
    }
}