package com.lime.minipay.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberDto {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateRequest {
        private String loginId;
        private String password;
        private String name;
    }
}
