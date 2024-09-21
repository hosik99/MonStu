package com.icetea.project.MonStu.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"member"})
public class AiContent {
    @Id
    @Column(name="content_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long aiContentId;

    @Lob  //@lob, columnDefinition -> 긴 텍스트 저장 시(oracle상황떄)
    @Column(name="content", columnDefinition = "CLOB")
    private String aiContent;

    @CreationTimestamp  //엔티티가 처음 생성될 때 현재 시간으로 필드를 자동으로 설정
    @Column(name="created_at",updatable = false)    //updatable->데이터베이스에서 수정되거나 업데이트되지 않도록 지정
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void setMember(Member member) {
        this.member = member;
        if(member != null && !member.getAiContents().contains(this)) member.getAiContents().add(this);
    }
}
