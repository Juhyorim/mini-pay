package com.lime.minipay.miniBoard;

import com.lime.minipay.entity.MiniBoard;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface MiniBoardRepository extends JpaRepository<MiniBoard, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select m from MiniBoard m where m.miniBoardId = :id")
    Optional<MiniBoard> findByIdWithLock(Long id);
}
