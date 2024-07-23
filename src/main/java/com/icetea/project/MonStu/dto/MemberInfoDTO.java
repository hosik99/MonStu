package com.icetea.project.MonStu.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MemberInfoDTO {
    private Long memberId;

    private LocalDate createdAt = LocalDate.now();

    private LocalDate birth;

    private String country;

    private String nickname;

}
