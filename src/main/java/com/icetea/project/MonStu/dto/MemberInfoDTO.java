package com.icetea.project.MonStu.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MemberInfoDTO {
    private Long memberId;

    private LocalDateTime createdAt = LocalDateTime.now();

    private int birth;

    private String country;

    private String nickname;

}
