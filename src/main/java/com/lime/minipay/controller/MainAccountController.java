package com.lime.minipay.controller;

import com.lime.minipay.dto.MainAccountDto;
import com.lime.minipay.dto.MainAccountDto.Response;
import com.lime.minipay.entity.Member;
import com.lime.minipay.service.MainAccountService;
import com.lime.minipay.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("main-account")
@RestController
public class MainAccountController {
    private final MainAccountService mainAccountService;
    private final MemberService memberService; //추후 open session in view 관련 공부 후 삭제예정

    @GetMapping
    public ResponseEntity<MainAccountDto.Response> getMainAccount(HttpServletRequest request) {
        Member member = (Member) request.getAttribute("member");
        member = memberService.findById(member.getMemberId());

        return ResponseEntity.ok(mainAccountService.getMainAccount(member));
    }

    @PostMapping("charge-cash")
    public ResponseEntity<MainAccountDto.Response> addCash(@RequestBody MainAccountDto.AddCashRequest request, HttpServletRequest httpServletRequest)
            throws InterruptedException {
        Member member = (Member) httpServletRequest.getAttribute("member");
        member = memberService.findById(member.getMemberId());

        Response response = mainAccountService.addCash(member, request);

        return ResponseEntity.ok(response);
    }
}
