package com.icetea.project.MonStu.controller;

import com.icetea.project.MonStu.dto.MyWordDTO;
import com.icetea.project.MonStu.service.MyWordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/member/myword")
public class MyWordController {

    private final MyWordService myWordSvc;

    public MyWordController(MyWordService myWordSvc) {
        this.myWordSvc = myWordSvc;
    }

    //save MyWords
    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody List<MyWordDTO> myWordDTOList){
        System.out.println("savewords - myWordDTOList : "+myWordDTOList);
        Boolean saved = myWordSvc.saveWords(myWordDTOList);
        return saved ?
                new ResponseEntity<>(HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.GATEWAY_TIMEOUT);
    }
}
