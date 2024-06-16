package com.lime.minipay;

import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DividedMoneys {
    private List<Long> moneys;
    private Long remain;

    public static DividedMoneys create(List<Long> moneys) {
        DividedMoneys dividedMoneys = new DividedMoneys();
        dividedMoneys.moneys = moneys;

        return dividedMoneys;
    }

    public static DividedMoneys createWithRemain(List<Long> moneys, Long remain) {
        DividedMoneys dividedMoneys = new DividedMoneys();
        dividedMoneys.moneys = moneys;
        dividedMoneys.remain = remain;

        return dividedMoneys;
    }
}
