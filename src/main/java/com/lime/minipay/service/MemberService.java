package com.lime.minipay.service;

import com.lime.minipay.dto.MemberDto;
import com.lime.minipay.entity.Member;
import com.lime.minipay.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public Member addMember(MemberDto.CreateRequest request) {
        Member member = Member.createMember(request.getLoginId(), request.getPassword(), request.getName());
        Member savedMember = memberRepository.save(member);

        return savedMember;
    }
}
