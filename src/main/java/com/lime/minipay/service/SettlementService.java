package com.lime.minipay.service;

import com.lime.minipay.DividedMoneys;
import com.lime.minipay.dto.SettlementDto;
import com.lime.minipay.dto.SettlementDto.Create;
import com.lime.minipay.entity.Member;
import com.lime.minipay.entity.MemberSettlement;
import com.lime.minipay.entity.Settlement;
import com.lime.minipay.entity.enums.SettlementType;
import com.lime.minipay.repository.MemberRepository;
import com.lime.minipay.repository.MemberSettlementRepository;
import com.lime.minipay.repository.SettlementRepository;
import com.lime.minipay.strategy.SettlementStrategy;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class SettlementService {
    private final SettlementRepository settlementRepository;
    private final MemberRepository memberRepository;
    private final MemberSettlementRepository memberSettlementRepository;
    private final List<SettlementStrategy> settlementStrategies;

    @Transactional
    public SettlementDto.Response create(Member member, Create request) {
        //settlement 생성
        Settlement settlement = Settlement.of(request.getAmount(), request.getType());
        settlement = settlementRepository.save(settlement);

        //돈 나누기
        DividedMoneys dividedMoneys = getDivideAmount(request.getType(), request.getAmount(),
                request.getParticipants().size() + 1);
        List<Long> moneys = dividedMoneys.getMoneys();
        settlement.setRemain(dividedMoneys.getRemain());

        //memberSettlement 생성
        List<MemberSettlement> memberSettlements = new ArrayList<>();
        int idx = 0;

        //채권자도 넣기
        MemberSettlement creditorMemberSettlement = MemberSettlement.of(settlement, member, member, moneys.get(idx++));
        creditorMemberSettlement.complete();
        memberSettlements.add(creditorMemberSettlement);

        //채무자들 넣기
        for (Long fromUserId : request.getParticipants()) {
            Member debtorMember = memberRepository.findById(fromUserId)
                    .orElseThrow(() -> new RuntimeException());

            MemberSettlement memberSettlement = MemberSettlement.of(settlement, member, debtorMember,
                    moneys.get(idx++));
            memberSettlement = memberSettlementRepository.save(memberSettlement);
            memberSettlements.add(memberSettlement);
        }

        return SettlementDto.Response.of(settlement, memberSettlements);
    }

    private DividedMoneys getDivideAmount(SettlementType type, Long amount, int cnt) {
        return settlementStrategies.stream()
                .filter(settlementStrategy -> settlementStrategy.isTarget(type))
                .findAny()
                .orElseThrow(() -> new RuntimeException("허용하지 않는 정산 유형입니다"))
                .divide(amount, cnt);
    }
}
