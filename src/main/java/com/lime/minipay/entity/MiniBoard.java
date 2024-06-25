package com.lime.minipay.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class MiniBoard {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long miniBoardId;

    private String title;

    private String content;

    //디폴트0
    private Long viewCount = 0L;

    public MiniBoard(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void increaseViewCount() {
        this.viewCount++;
    }
}
