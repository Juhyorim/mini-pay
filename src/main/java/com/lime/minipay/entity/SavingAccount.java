package com.lime.minipay.entity;

import com.lime.minipay.entity.enums.SavingAccountType;
import com.lime.minipay.error.NoMoneyEnoughException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class SavingAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long savingAccountId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_account_id", referencedColumnName = "mainAccountId", nullable = false)
    private MainAccount mainAccount;

    @Column(nullable = false)
    private Long principal = 0L;

    @Column(nullable = false)
    private Long interest = 0L;

    @Enumerated(EnumType.STRING)
    private SavingAccountType type;

    public static SavingAccount of(MainAccount mainAccount) {
        SavingAccount savingAccount = new SavingAccount();
        savingAccount.mainAccount = mainAccount;

        return savingAccount;
    }

    public void addCash(Long amount) {
        if (mainAccount.getBalance() < amount) {
            throw new NoMoneyEnoughException();
        }

        mainAccount.transferCash(amount);
        this.principal += amount;
    }

    public Long getBalance() {
        return this.principal + this.interest;
    }

    public Long getSavingAccountId() {
        return this.savingAccountId;
    }

    public MainAccount getMainAccount() {
        return this.mainAccount;
    }
}
