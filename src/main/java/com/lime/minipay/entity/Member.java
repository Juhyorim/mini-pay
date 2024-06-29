package com.lime.minipay.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false, unique = true)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @OneToOne(mappedBy = "member", fetch = FetchType.LAZY)
    private MainAccount mainAccount;

    public static Member createMember(String login_id, String password, String name) {
        Member newMember = new Member();
        newMember.loginId = login_id;
        newMember.password = password;
        newMember.name = name;

        return newMember;
    }

    public void addMainAccount(MainAccount mainAccount) {
        if (this.mainAccount == null)
            this.mainAccount = mainAccount;
        else
            throw new RuntimeException();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }

        Member member = (Member) o;
        return Long.compare(this.memberId, member.memberId) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId);
    }
}
