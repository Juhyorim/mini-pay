package com.lime.minipay;

import com.lime.minipay.dto.MainAccountDto.AddCashRequest;
import com.lime.minipay.entity.Member;
import com.lime.minipay.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AccountServiceTest {

    @Autowired
    private com.lime.minipay.service.MainAccountService accountService;

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
        Thread thread3 = new Thread(task);
        Thread thread4 = new Thread(task);
        Thread thread5 = new Thread(task);
        Thread thread6 = new Thread(task);

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();
        thread6.start();

        thread1.join();
        thread2.join();
        thread3.join();
        thread4.join();
        thread5.join();
        thread6.join();
    }
}