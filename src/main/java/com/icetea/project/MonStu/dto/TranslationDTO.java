package com.icetea.project.MonStu.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TranslationDTO {

    private final String source;
    private final String target;
    private final String text;

    public TranslationDTO(String source, String target, String text) {
        this.source = source;
        this.target = target;
        this.text = text;
    }


}

