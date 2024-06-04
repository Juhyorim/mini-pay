package com.lime.minipay.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class SavingAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long savingAccountId;

    @Column(nullable = false)
    @ManyToOne
    @JoinColumn(name = "main_account_id", referencedColumnName = "mainAccountId")
    private MainAccount mainAccount;

    @Column(nullable = false)
    private Long balance = 0L;
}
