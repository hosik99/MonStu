package com.icetea.project.MonStu.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MySentenceDTO {

    private Long mySentenceId;

    private String sentence;

    private String type;
}
