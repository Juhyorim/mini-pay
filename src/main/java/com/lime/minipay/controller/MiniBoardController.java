package com.lime.minipay.controller;

import com.lime.minipay.entity.MiniBoard;
import com.lime.minipay.repository.MiniBoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Transactional
@RequiredArgsConstructor
@RequestMapping("mini-board")
@RestController
public class MiniBoardController {
    private final MiniBoardRepository miniBoardRepository;

    @PostMapping("create")
    public ResponseEntity create() {
        MiniBoard miniBoard = new MiniBoard(null, "title", "content");
        miniBoardRepository.save(miniBoard);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("update-title")
    public ResponseEntity update(@RequestBody String name) throws InterruptedException {
        MiniBoard miniBoard = miniBoardRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException());
        log.info("타이틀 변경: " + miniBoard.getTitle() + " => " + name);
        miniBoard.setTitle(name);
        miniBoardRepository.save(miniBoard);
        Thread.sleep(7000);

        return ResponseEntity.ok().build();
    }
}
