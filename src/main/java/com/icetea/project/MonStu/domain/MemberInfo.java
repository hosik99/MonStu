package com.icetea.project.MonStu.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Entity
@ToString(exclude = "member")
@Table(name="member_info")
public class MemberInfo {

    //user.class의 기본키
    @Id @Column(name="member_id")
    private Long memberId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "birth")
    private int birth;

    @Column(name = "country")
    private String country;

    @Column(name = "nickname")
    private String nickname;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // 이 엔티티의 Primary Key가 다른 엔티티의 Primary Key를 참조하도록 설정합니다.
    @JoinColumn(name = "member_id")   //@JoinColumn 어노테이션은 외래 키 컬럼을 정의합니다.
    private Member member;

    public void setUser(Member member) {this.member = member;}
}
