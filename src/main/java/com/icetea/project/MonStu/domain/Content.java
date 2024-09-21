package com.icetea.project.MonStu.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@ToString(exclude = {"member","myWord","mySentence"})
@Table(name="content")
public class Content {

    @Id @Column(name="content_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contentId;

    @Column(name="title")
    private String title;

    @Lob  //@lob, columnDefinition -> 긴 텍스트 저장 시(oracle상황떄)
    @Column(name="content", columnDefinition = "CLOB")
    private String content;

    @CreationTimestamp  //엔티티가 처음 생성될 때 현재 시간으로 필드를 자동으로 설정
    @Column(name="created_at",updatable = false)    //updatable->데이터베이스에서 수정되거나 업데이트되지 않도록 지정
    private LocalDateTime createdAt;

    @UpdateTimestamp    //엔티티가 업데이트될 때 현재 시간으로 필드를 자동으로 설정
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void setMember(Member member) {
        this.member = member;
        if(!member.getContents().contains(this)) member.getContents().add(this);
    }

    @OneToMany(mappedBy = "content",cascade = CascadeType.ALL,orphanRemoval = true) //orphanRemoval->연결이 끊어지면 고아되는 객체 삭제
    private List<MyWord> myWords = new ArrayList<>();

    public void addMyWords(MyWord myword){
        this.myWords.add(myword);
        if(myword.getContent()!=this) myword.setContent(this);
    }

    public void removeMyWords(MyWord myword){
        myWords.remove(myword);
        if(myword.getContent()==this) myword.setContent(null);
    }

    @OneToMany(mappedBy = "content",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<MySentence> mySentences = new ArrayList<>();

    public void addMyWords(MySentence mySentence){
        this.mySentences.add(mySentence);
        if(mySentence.getContent()!=this) mySentence.setContent(this);
    }

    public void removeMyWords(MySentence mySentence){
        mySentences.remove(mySentence);
        if(mySentence.getContent()==this) mySentence.setContent(null);
    }
}