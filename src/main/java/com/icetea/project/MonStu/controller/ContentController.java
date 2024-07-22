package com.icetea.project.MonStu.controller;

import com.icetea.project.MonStu.dto.ContentDTO;
import com.icetea.project.MonStu.enums.ResponseMsg;
import com.icetea.project.MonStu.service.ContentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member/content")
public class ContentController {

    private static final Logger log = LoggerFactory.getLogger(ContentController.class);
    private ContentService conSvc;

    public ContentController(ContentService conSvc) { this.conSvc = conSvc; }

    //test
    @PostMapping("/add")
    public ResponseEntity<String> saveContent(@RequestBody ContentDTO contentDTO){
        log.info("ContentDTO : {}",contentDTO.toString());
        Boolean isSaved = conSvc.saveContent(contentDTO);
        return isSaved ?
                new ResponseEntity<>(ResponseMsg.SAVE_FAILURE.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR) :
                new ResponseEntity<>(ResponseMsg.SAVE_SUCCESS.getMessage(),HttpStatus.OK);
    }

    @GetMapping("/showTitles")
    public String showTitles(){
        return "awdawdawdawdad";
    }






}
