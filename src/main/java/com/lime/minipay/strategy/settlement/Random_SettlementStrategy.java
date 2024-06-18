package com.lime.minipay.strategy.settlement;

import com.lime.minipay.DividedMoneys;
import com.lime.minipay.entity.enums.SettlementType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.springframework.stereotype.Component;

@Component
public class Random_SettlementStrategy implements SettlementStrategy {
    @Override
    public DividedMoneys divide(Long amount, int cnt) {
        List<Long> moneys = new ArrayList<>();
        Random rand = new Random();

        //@TODO 랜덤 생성기 구현
        for (int i =0; i<cnt; i++) {
            long value = rand.nextLong(amount - 1) + 1;
            moneys.add(value);
            amount -= value;
        }

        Collections.sort(moneys);

        return DividedMoneys.create(moneys);
    }

    @Override
    public boolean isTarget(SettlementType settlementType) {
        return settlementType == SettlementType.RANDOM;
    }
}
