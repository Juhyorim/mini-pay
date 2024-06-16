package com.lime.minipay.entity;

import com.lime.minipay.entity.enums.SettlementType;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Settlement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long settlementId;

    private Long amount;

    @Enumerated(EnumType.STRING)
    private SettlementType type;

    @Column(insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    private Long remainAmount;

    public static Settlement of(Long amount, SettlementType type) {
        Settlement settlement = new Settlement(null, amount, type, null, 0L);

        return settlement;
    }

    public void setRemain(Long remainAmount) {
        this.remainAmount = remainAmount;
    }
}
