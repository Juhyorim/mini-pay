package com.lime.minipay.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class SavingAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long savingAccountId;

    @ManyToOne
    @JoinColumn(name = "main_account_id", referencedColumnName = "mainAccountId", nullable = false)
    private MainAccount mainAccount;

    @Column(nullable = false)
    private Long balance = 0L;

    public static SavingAccount of(MainAccount mainAccount) {
        SavingAccount savingAccount = new SavingAccount();
        savingAccount.mainAccount = mainAccount;

        return savingAccount;
    }
}
