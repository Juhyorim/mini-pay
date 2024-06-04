package com.lime.minipay.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "main_account")
public class MainAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mainAccountId;

    @OneToOne
    @JoinColumn(name = "member_id", referencedColumnName = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private Long balance = 0L; //잔액

    @Column(nullable = false)
    private Long dayCharged = 0L; //오늘 충전된 양

    @Column(nullable = false)
    private Long chargeLimit; //1일 충전 한도
}
