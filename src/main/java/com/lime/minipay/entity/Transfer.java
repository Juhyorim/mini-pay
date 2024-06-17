package com.lime.minipay.entity;

import com.lime.minipay.entity.enums.TransferStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transferId;

    @ManyToOne
    @JoinColumn(name = "from_account_id", referencedColumnName = "mainAccountId")
    private MainAccount fromAccount;

    @ManyToOne
    @JoinColumn(name = "to_account_id", referencedColumnName = "mainAccountId")
    private MainAccount toAccount;

    private Long amount;

    @Column(insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "transfer_status")
    private TransferStatus status = TransferStatus.PENDING;

    public static Transfer of(MainAccount fromAccount, MainAccount toAccount, Long amount) {
        Transfer transfer = new Transfer();
        transfer.fromAccount = fromAccount;
        transfer.toAccount = toAccount;
        transfer.amount = amount;

        //돈 빠지기
        fromAccount.transferCash(amount);

        return transfer;
    }
}
