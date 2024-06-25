package com.lime.minipay.service;

import com.lime.minipay.dto.MemberDto;
import com.lime.minipay.entity.MainAccount;
import com.lime.minipay.entity.Member;
import com.lime.minipay.repository.MainAccountRepository;
import com.lime.minipay.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final MainAccountRepository mainAccountRepository;
    private final JwtService jwtService;

    private static final Long defaultChargeLimit = 3_000_000L;

    @Transactional
    public Member addMember(MemberDto.CreateRequest request) {
        Member member = Member.createMember(request.getLoginId(), request.getPassword(), request.getName());
        Member savedMember = memberRepository.save(member);

        //메인 계좌 생성
        MainAccount mainAccount = MainAccount.of(savedMember, defaultChargeLimit);
        mainAccountRepository.save(mainAccount);

        return savedMember;
    }

    public MemberDto.LoginResponse login(MemberDto.LoginRequest request) {
        memberRepository.findByLoginIdAndPassword(request.getLoginId(), request.getPassword())
                .orElseThrow(() -> new RuntimeException());

        //jwt 토큰 발급
        String accessToken = jwtService.createJwt(request.getLoginId());

        return MemberDto.LoginResponse.builder()
                .accessToken(accessToken)
                .build();
    }

    public Member getUserByLoginId(String loginId) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new RuntimeException());

        return member;
    }

    public Member findById(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException());

        return member;
    }
}
