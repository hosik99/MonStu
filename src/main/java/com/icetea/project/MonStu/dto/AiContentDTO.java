package com.icetea.project.MonStu.dto;

import com.icetea.project.MonStu.domain.Member;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class AiContentDTO {

    private Long aiContentId;

    private String aiContent;

    private LocalDateTime createdAt;

    private String email;



    @QueryProjection
    public AiContentDTO(Long aiContentId, String aiContent, LocalDateTime createdAt, String email) {
        this.aiContentId = aiContentId;
        this.aiContent = aiContent;
        this.createdAt = createdAt;
        this.email = email;
    }

}
