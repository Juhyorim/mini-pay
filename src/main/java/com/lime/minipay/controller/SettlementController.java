package com.lime.minipay.controller;

import com.lime.minipay.dto.SettlementDto;
import com.lime.minipay.dto.SettlementDto.Create;
import com.lime.minipay.entity.Member;
import com.lime.minipay.service.MemberService;
import com.lime.minipay.service.SettlementService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("settlement")
@RestController
public class SettlementController {
    private final SettlementService settlementService;
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity create(@RequestBody Create request,
                                 HttpServletRequest httpServletRequest) {
        Member member = (Member) httpServletRequest.getAttribute("member");
        member = memberService.findById(member.getMemberId());

        log.info(String.valueOf(request.getType()));

        SettlementDto.Response response = settlementService.create(member, request);

        return ResponseEntity.ok(response);
    }
}
