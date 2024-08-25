package com.icetea.project.MonStu.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MyWordDTO {

    private Long myWordId;

    private String targetWord;

    private String translatedWord;

    private Long contentId;

    @QueryProjection
    public MyWordDTO(Long myWordId, String targetWord, String translatedWord) {
        this.myWordId = myWordId;
        this.targetWord = targetWord;
        this.translatedWord = translatedWord;
    }

}
