package com.lime.minipay;

import com.lime.minipay.dto.MainAccountDto.AddCashRequest;
import com.lime.minipay.entity.MainAccount;
import com.lime.minipay.entity.Member;
import com.lime.minipay.repository.MainAccountRepository;
import com.lime.minipay.repository.MemberRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.concurrent.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AccountServiceConcurrencyTest {

    @Autowired
    private com.lime.minipay.service.MainAccountService accountService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MainAccountRepository mainAccountRepository;

    private ExecutorService executorService;

    @BeforeEach
    public void setUp() {
        executorService = Executors.newFixedThreadPool(2);
    }

    @Test
    public void testAddCashConcurrency() throws InterruptedException, ExecutionException {
        Member member = memberRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException());

        Callable<Void> task = () -> {
            AddCashRequest request = new AddCashRequest(100L);
            accountService.addCash(member, request);
            return null;
        };

        Future<Void> future1 = executorService.submit(task);
        Future<Void> future2 = executorService.submit(task);

        future1.get();
        future2.get();

        // 추가 검증 로직
        MainAccount account = mainAccountRepository.findByMember(member).orElseThrow(() -> new RuntimeException());
        System.out.println("Final balance: " + account.getBalance());
    }

    @AfterEach
    public void tearDown() {
        executorService.shutdown();
    }
}
