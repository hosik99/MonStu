package com.icetea.project.MonStu.dto;


import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ContentDTO {

    private Long contentId;

    private String title;

    private String content;

    private LocalDateTime createdAt;

    private String email;

    @QueryProjection
    public ContentDTO(Long contentId, String title, String content, LocalDateTime createdAt, String email) {
        this.contentId = contentId;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.email = email;
    }
}
