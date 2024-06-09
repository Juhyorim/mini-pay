package com.lime.minipay.repository;

import com.lime.minipay.entity.MiniBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MiniBoardRepository extends JpaRepository<MiniBoard, Long> {
}
