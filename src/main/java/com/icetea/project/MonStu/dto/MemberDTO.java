package com.icetea.project.MonStu.dto;

import com.icetea.project.MonStu.enums.Role;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {
    private Long memberId;

    private String email;

    private String memberPw;

    private Role role = Role.ROLE_MEMBER;   //Default -> member;
}
