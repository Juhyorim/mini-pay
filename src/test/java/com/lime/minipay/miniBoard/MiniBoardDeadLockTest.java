package com.lime.minipay.miniBoard;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MiniBoardDeadLockTest {
    private static final int COUNT = 100;
    private static final ExecutorService service = Executors.newFixedThreadPool(COUNT);

    @Autowired
    private MiniBoardController miniBoardController;

    @Autowired
    private MiniBoardRepository miniBoardRepository;

    @Test
    @DisplayName("요청 수 만큼 숫자 증가 (Normal)")
    void incrementNumber_normal() throws InterruptedException {
        // given
        CountDownLatch latch = new CountDownLatch(COUNT);
        Long beforeCnt = miniBoardRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException()).getViewCount();

        // when
        for (int i = 0; i < COUNT; ++i) {
            service.execute(() -> {
                miniBoardController.viewBoardNormal(1L);
                latch.countDown();
            });
        }
        // then
        latch.await();

        Long afterCnt = miniBoardRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException()).getViewCount();

        Assertions.assertEquals(beforeCnt + COUNT, afterCnt);
    }
    //expected: <100> but was: <13>
    //Expected :100
    //Actual   :13

    @Test
    @DisplayName("요청 수 만큼 숫자 증가 (Pessimistic Lock)")
    void incrementNumber_Pessimistic() throws InterruptedException {
        // given
        CountDownLatch latch = new CountDownLatch(COUNT);
        Long beforeCnt = miniBoardRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException()).getViewCount();

        // when
        for (int i = 0; i < COUNT; ++i) {
            service.execute(() -> {
                miniBoardController.viewBoardWithLock(1L);
                latch.countDown();
            });
        }
        // then
        latch.await();

        Long afterCnt = miniBoardRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException()).getViewCount();

        Assertions.assertEquals(beforeCnt + COUNT, afterCnt);
    }
    //success

    @Test
    @DisplayName("요청 수 만큼 숫자 증가 (Serializable)")
    void incrementNumber_Serializable() throws InterruptedException {
        // given
        CountDownLatch latch = new CountDownLatch(COUNT);
        Long beforeCnt = miniBoardRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException()).getViewCount();

        // when
        for (int i = 0; i < COUNT; ++i) {
            service.execute(() -> {
                miniBoardController.viewBoardWithSerializable(1L);
                latch.countDown();
            });
        }
        // then
        latch.await();

        Long afterCnt = miniBoardRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException()).getViewCount();

        Assertions.assertEquals(beforeCnt + COUNT, afterCnt);
    }
    //엄청나게 오랜 시간이 걸림
}
