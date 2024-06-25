package com.lime.minipay.miniBoard;

import com.lime.minipay.entity.MiniBoard;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    private final MiniBoardService miniBoardService;
    private final MiniBoardRepository miniBoardRepository;
    private final PlatformTransactionManager tm;

    @PostMapping("create")
    public ResponseEntity create() {
        MiniBoard miniBoard = new MiniBoard("title", "content");
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

    @PatchMapping("{boardId}/view/noLock")
    public ResponseEntity viewBoardNormal(@PathVariable Long boardId) {
        MiniBoard miniBoard = miniBoardRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException());

        miniBoard.increaseViewCount();

        return ResponseEntity.ok().build();
    }

    @PatchMapping("{boardId}/view/lock")
    public ResponseEntity viewBoardWithLock(@PathVariable Long boardId) {
        MiniBoard miniBoard = miniBoardRepository.findByIdWithLock(1L)
                .orElseThrow(() -> new RuntimeException());

        miniBoard.increaseViewCount();

        return ResponseEntity.ok().build();
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @PatchMapping("{boardId}/view/serializable")
    public ResponseEntity viewBoardWithSerializable(@PathVariable Long boardId) {
        MiniBoard miniBoard = miniBoardRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException());

        miniBoard.increaseViewCount();

        return ResponseEntity.ok().build();
    }

    @PatchMapping("{boardId}/view/tx")
    public ResponseEntity viewBoardWithTxManager(@PathVariable Long boardId) {
        miniBoardService.viewBoardWithTxManager(boardId);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("{boardId}/view/tx-template")
    public ResponseEntity viewBoardWithTemplate(@PathVariable Long boardId) {
        miniBoardService.viewBoardWithTxTemplate(boardId);

        return ResponseEntity.ok().build();
    }
}
