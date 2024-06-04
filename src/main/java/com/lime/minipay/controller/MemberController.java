package com.lime.minipay.controller;

import com.lime.minipay.dto.MemberDto;
import com.lime.minipay.dto.MemberDto.LoginResponse;
import com.lime.minipay.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("member")
@RestController
public class MemberController {
    private final MemberService memberService;

    @PostMapping("signup")
    public ResponseEntity signup(@RequestBody MemberDto.CreateRequest request) {
        memberService.addMember(request);

        return ResponseEntity.ok().build();
    }

    @PostMapping("login")
    public ResponseEntity login(@RequestBody MemberDto.LoginRequest request) {
        LoginResponse response = memberService.login(request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("login-test")
    public ResponseEntity loginTest() {
        return ResponseEntity.ok("hi");
    }
}
