package com.lime.minipay.controller;

import com.lime.minipay.dto.MainAccountDto;
import com.lime.minipay.dto.MainAccountDto.Response;
import com.lime.minipay.dto.SavingAccountDto;
import com.lime.minipay.dto.SavingAccountDto.GetAll;
import com.lime.minipay.entity.Member;
import com.lime.minipay.service.MemberService;
import com.lime.minipay.service.SavingAccountService;
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
@RequestMapping("saving-account")
@RestController
public class SavingAccountController {
    private final MemberService memberService;
    private final SavingAccountService savingAccountService;

    @PostMapping("add")
    public ResponseEntity addNewSavingAccount(HttpServletRequest request) {
        Member member = (Member) request.getAttribute("member");
        member = memberService.findById(member.getMemberId());

        savingAccountService.add(member);

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity getSavingAccountList(HttpServletRequest request) {
        Member member = (Member) request.getAttribute("member");
        member = memberService.findById(member.getMemberId());

        GetAll list = savingAccountService.find(member);

        return ResponseEntity.ok(list);
    }

    @PostMapping("charge-cash")
    public ResponseEntity<MainAccountDto.Response> chargeCash(@RequestBody SavingAccountDto.ChargeRequest request, HttpServletRequest httpServletRequest) {
        Member member = (Member) httpServletRequest.getAttribute("member");
        member = memberService.findById(member.getMemberId());

        Response response = savingAccountService.chargeCash(member, request);

        return ResponseEntity.ok(response);
    }
}
