package com.lime.minipay.dto;

import com.lime.minipay.entity.SavingAccount;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class SavingAccountDto {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetAll {
        private List<Info> list;

        public static GetAll of(List<SavingAccount> list) {
            return new GetAll(list.stream()
                    .map((sa) -> new Info(sa.getSavingAccountId(), sa.getBalance()))
                    .collect(Collectors.toList())
            );
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Info {
        private Long savingAccountId;
        private Long balance;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChargeRequest {
        private Long amount;
        private Long savingAccountId;
    }
}
