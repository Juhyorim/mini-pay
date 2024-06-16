package com.lime.minipay.strategy;

import com.lime.minipay.DividedMoneys;
import com.lime.minipay.entity.enums.SettlementType;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class NDivide_SettlementStrategy implements SettlementStrategy {
    @Override
    public DividedMoneys divide(Long amount, int cnt) {
        List<Long> moneys = new ArrayList<>();
        Long value = amount / cnt;
        Long remain = amount - value * cnt;

        for (int i = 0; i < cnt; i++) {
            moneys.add(value);
        }

        return DividedMoneys.createWithRemain(moneys, remain);
    }

    @Override
    public boolean isTarget(SettlementType settlementType) {
        return settlementType == SettlementType.N_DIVIDE;
    }
}
