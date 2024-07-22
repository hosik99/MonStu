package com.icetea.project.MonStu.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@ToString(exclude = "member")
@Table(name="pw_quiz")
public class PwQuiz {

    /*
     GenerationType.AUTO : JPA 구현체가 데이터베이스에 적합한 기본 키 생성 전략을 자동으로 선택
     GenerationType.IDENTITY : 일반적으로 AUTO_INCREMENT 속성을 사용하는 MySQL 같은 데이터베이스에서 사용
     GenerationType.SEQUENCE : 데이터베이스 시퀀스를 사용하여 기본 키를 생성합니다. 주로 Oracle 같은 시퀀스를 지원하는 데이터베이스에서 사용됩니다.
     GenerationType.TABLE :별도의 테이블을 사용하여 기본 키를 생성합니다. 모든 데이터베이스에서 사용할 수 있으며, 특정 시퀀스나 IDENTITY 기능이 없는 데이터베이스에서 사용할 수 있습니다.
    */
    @Id
    @Column(name="qid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qid;

    @Column(name="quiz")
    private String quiz;

    //cascade -> 부모 수정 시 자식도 수정 ALL
    //orphanRemoval -> PwQuiz에서 제거된 users 엔티티 자동 삭제,주로 리스트나 컬렉션을 사용하는 관계에서 자식 엔티티가 부모 엔티티에서 제거될 때 자동으로 삭제되도록 하기 위해 사용됩니다. @OneToOne 관계에서는 일반적으로 필요하지 않습니다.
    //mappedBy -> 매핑할 상대 필드(ManyToOne필드명)
    @OneToMany(mappedBy = "pwQuiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Member> members = new ArrayList<>();

    public void addUser(Member member) {
        this.members.add(member);
        if(member.getPwQuiz()!=this) member.setPwQuiz(this);
    }

    public void removeUser(Member member) {
        members.remove(member);
        if(member.getPwQuiz() == this) member.setPwQuiz(null);
    }
}
