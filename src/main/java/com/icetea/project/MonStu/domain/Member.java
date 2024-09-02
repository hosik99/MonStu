package com.icetea.project.MonStu.domain;


import com.icetea.project.MonStu.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

//@Data   //equals, hashCode, toString, @EqualsAndHashCode, @ToString, @Getter, @Setter
//@Slf4j
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"pwQuiz","content"})
@Table(name="member")
public class Member {

    @Id @Column(name="member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

       //Login할떄 사용
    @Column(name = "email")
    private String email;

       //pw답 (비밀번호)
    @Column(name="member_pw")
    private String memberPw;

    /*EnumType.ORDINAL : enum 순서 값을 DB에 저장
	  EnumType.STRING : enum 이름을 DB에 저장*/
    @Enumerated(EnumType.STRING)    //권한
    @Column(name="role")
    private Role role;

    //mappedBy -> memberInfo의 member field
    //optional = false -> 항상 값을 가져야 하며, null일 수 없음을 나타냅니다.
    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private MemberInfo memberInfo;

    public void setMemberInfo(MemberInfo memberInfo) {
        this.memberInfo = memberInfo;
        memberInfo.setMember(this);
    }

    /*
    CascadeType.ALL: 모든 작업(저장, 수정, 삭제 등)이 연관된 엔티티에 전파됩니다.
    CascadeType.PERSIST: 저장(영속화) 작업이 연관된 엔티티에 전파됩니다.
    CascadeType.MERGE: 수정(병합) 작업이 연관된 엔티티에 전파됩니다.
    CascadeType.REMOVE: 삭제 작업이 연관된 엔티티에 전파됩니다.
    CascadeType.REFRESH: 엔티티를 refresh할 때 연관된 엔티티에 전파됩니다.
    CascadeType.DETACH: 엔티티를 detach할 때 연관된 엔티티에 전파됩니다.
    */
    /*
    FetchType.LAZY: 연관된 엔티티를 실제로 사용할 때까지 데이터베이스에서 가져오지 않습니다. 지연 로딩 방식입니다.
    FetchType.EAGER: 부모 엔티티를 조회할 때 연관된 엔티티를 즉시 데이터베이스에서 함께 가져옵니다. 즉시 로딩 방식입니다.
                     성능 이슈를 발생시킬 수 있으므로 필요한 경우에만 사용해야 합니다.
    */
    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Content> contents = new ArrayList<>();

    public void addContent(Content content){
        this.contents.add(content);
        if(content.getMember()!=this) content.setMember(this);
    }

    public void removeContent(Content content){
        contents.remove(content);
        if(content.getMember()==this) content.setMember(null);
    }
}
