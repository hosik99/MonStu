package com.icetea.project.MonStu.dto;


import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
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

    private LocalDateTime createdAt = LocalDateTime.now();

    private String memberId;
}
