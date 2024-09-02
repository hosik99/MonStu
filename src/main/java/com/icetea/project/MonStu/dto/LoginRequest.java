package com.icetea.project.MonStu.dto;

import lombok.*;

/*
* Login 정보를 담는 DTO
* */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    private String email;

    private String memberPw;
}
