package com.icetea.project.MonStu.controller;

import com.icetea.project.MonStu.dto.MemberDTO;
import com.icetea.project.MonStu.dto.MemberInfoDTO;
import com.icetea.project.MonStu.dto.SignUpDTO;
import com.icetea.project.MonStu.enums.ResponseMsg;
import com.icetea.project.MonStu.service.SignService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sign")
public class SignController {

    private static final Logger log = LoggerFactory.getLogger(SignController.class);
    private final SignService signSvc;

    public SignController(SignService signSvc) {
        this.signSvc = signSvc;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody SignUpDTO signUpDTO){
        MemberDTO memberDTO = signUpDTO.getMemberDTO();
        MemberInfoDTO memberInfoDTO = signUpDTO.getMemberInfoDTO();
        log.info("memberDTO : {}",memberDTO.toString());
        log.info("memberInfoDTO : {}",memberInfoDTO.toString());
        Boolean isSaved = signSvc.saveUser(memberDTO, memberInfoDTO);

        return isSaved ?
                new ResponseEntity<>(ResponseMsg.SIGNUP_FAILURE.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR) :
                new ResponseEntity<>(ResponseMsg.SIGNUP_SUCCESS.getMessage(),HttpStatus.OK);
    }
}
