package com.icetea.project.MonStu.controller;

import com.icetea.project.MonStu.dto.MyWordDTO;
import com.icetea.project.MonStu.dto.TranslationDTO;
import com.icetea.project.MonStu.service.TranslationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/tr")
public class TranslationController {

    private final TranslationService translationService;

    public TranslationController(TranslationService translationService) {
        this.translationService = translationService;
    }

    //Translation (used NAVER-PAPAGO AI)
    @PostMapping("/api/translation")
    public String Translation(@RequestBody TranslationDTO translationDTO){
                    log.info(translationDTO.toString());
        return translationService.translateText(translationDTO);
    }

}
