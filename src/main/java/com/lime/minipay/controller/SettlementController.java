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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @PostMapping("{memberSettlementId}")
    public ResponseEntity completeSettlement(@PathVariable(name = "memberSettlementId") Long memberSettlementId,
                                             HttpServletRequest httpServletRequest) {
        Member member = (Member) httpServletRequest.getAttribute("member");
        member = memberService.findById(member.getMemberId());

        settlementService.complete(member, memberSettlementId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("{settlementId}")
    public ResponseEntity getInfo(@PathVariable(name = "settlementId") Long settlementId,
                                  HttpServletRequest httpServletRequest) {
        SettlementDto.Response response = settlementService.getInfo(settlementId);

        return ResponseEntity.ok(response);
    }
}
