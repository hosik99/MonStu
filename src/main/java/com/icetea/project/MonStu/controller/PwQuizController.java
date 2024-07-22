package com.icetea.project.MonStu.controller;

import com.icetea.project.MonStu.domain.PwQuiz;
import com.icetea.project.MonStu.dto.PwQuizDTO;
import com.icetea.project.MonStu.service.PwQuizService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/* PasswordQuiz Management Page for admin*/
@Slf4j
@RestController
@RequestMapping("/admin/pwquiz")
public class PwQuizController {

    private PwQuizService pwQuizService;

    public PwQuizController(PwQuizService pwQuizService) {
        this.pwQuizService = pwQuizService;
    }

    //Get "pwQuiz" All Data
    @GetMapping("/findAll")
    public List<PwQuizDTO> getAllPwQuiz() {
        return pwQuizService.findAllPwQuiz();
    }

    @PostMapping("/save")
    public ResponseEntity<String> addOnePwQuiz(@RequestBody PwQuizDTO pwQuizDTO) {
        try {
            boolean isSaved = pwQuizService.savePwQuiz(pwQuizDTO);
            if(isSaved){
                return new ResponseEntity<>("저장되었습니다.",HttpStatus.OK);
            }else{
                return new ResponseEntity<>("저장하는데 실패했습니다. 다시 시도해주세요.",HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch (Exception e){
            return new ResponseEntity<>("오류가 발생했습니다. 다시 시도해주세요.",HttpStatus.GATEWAY_TIMEOUT);
        }
    }
}
