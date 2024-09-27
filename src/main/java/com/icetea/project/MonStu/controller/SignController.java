package com.icetea.project.MonStu.controller;

import com.icetea.project.MonStu.dto.MemberDTO;
import com.icetea.project.MonStu.dto.MemberInfoDTO;
import com.icetea.project.MonStu.dto.SignUpDTO;
import com.icetea.project.MonStu.enums.EmailMsg;
import com.icetea.project.MonStu.enums.ResponseMsg;
import com.icetea.project.MonStu.service.SignService;
import lombok.extern.slf4j.Slf4j;
import oracle.jdbc.proxy.annotation.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/sign")
public class SignController {

//    private static final Logger log = LoggerFactory.getLogger(SignController.class);
    private final SignService signSvc;

    public SignController(SignService signSvc) {
        this.signSvc = signSvc;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody SignUpDTO signUpDTO){
        MemberDTO memberDTO = signUpDTO.getMemberDTO();
        MemberInfoDTO memberInfoDTO = signUpDTO.getMemberInfoDTO();
        log.info("memberDTO: "+memberDTO.toString());
        Boolean isSaved = signSvc.saveMember(memberDTO, memberInfoDTO);
        return isSaved ?
                new ResponseEntity<>(ResponseMsg.SIGNUP_SUCCESS.getMessage(), HttpStatus.OK) :
                new ResponseEntity<>(ResponseMsg.SIGNUP_FAILURE.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
    }


    /*
        HttpStatus.CONFLICT (409 Conflict) -> 주로 리소스의 중복성이나 충돌을 알리기 위해 사용
    */
    //CHECK EMAIL REDUNDANCY
    @PostMapping("/check")
    public ResponseEntity<Map<String, Object>> checkEmail(@RequestBody Map<String, Object> requestData){
        String email = (String) requestData.get("email");
        Map<String, Object> response = new HashMap<>();

        if ( signSvc.existsByEmail(email) ) {
            response.put("stateMessage", ResponseMsg.SIGNUP_EXISTS_EMAIL.getMessage());
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        } else {
            response.put("stateMessage", ResponseMsg.SIGNUP_NONEXISTS_EMAIL.getMessage());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    //SEND EMAIL CHECK CODE
    @PostMapping("/emailcode")
    public ResponseEntity< Map<String, Object> > emailCode(@RequestBody Map<String, Object> requestData){
        String email = (String) requestData.get("email");
        String checkCode = signSvc.sendEmailCode(email);   //전송 실패 시 null 반환

        Map<String, Object> response = new HashMap<>();
        if (checkCode != null) {
            response.put("checkCode", checkCode);
            response.put("stateMessage", EmailMsg.SIGNUP_SUCCESS.getMessage());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("stateMessage", EmailMsg.SIGNUP_FAILURE.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
