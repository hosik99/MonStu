package com.icetea.project.MonStu.controller;

import com.icetea.project.MonStu.dto.ContentDTO;
import com.icetea.project.MonStu.dto.MyWordDTO;
import com.icetea.project.MonStu.enums.ResponseMsg;
import com.icetea.project.MonStu.service.ContentService;
import com.icetea.project.MonStu.service.WordService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/member/content")
public class ContentController {

    private final ContentService conSvc;
    private final WordService wordSvc;

    public ContentController(ContentService conSvc,WordService wordSvc) {
        this.conSvc = conSvc;
        this.wordSvc = wordSvc;
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> saveContent(@RequestBody ContentDTO contentDTO){
        Map<String, Object> response = new HashMap<>();
                    log.info("ContentController-saveContent : New Content Save Request");
        try {
            conSvc.saveContent(contentDTO);
            response.put("stateMessage",ResponseMsg.SAVE_SUCCESS.getMessage());
            return new ResponseEntity<>(response,HttpStatus.CREATED);  //201
        }catch (Exception e){
            response.put("stateMessage",ResponseMsg.SAVE_FAILURE.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);   //500
        }
    }

    //GET ALL CONTENTDTOs
    @GetMapping("/getContents")
    public ResponseEntity<Map<String, Object>> getContents(){
        List<ContentDTO> contentDTOList = conSvc.getContentsByEmail();
        Map<String, Object> response = new HashMap<>();
        if (!contentDTOList.isEmpty()) {
            response.put("contentDTOList", contentDTOList);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return ResponseEntity.noContent().build();  // 204 No Content 상태 반환, 요청은 성공했으나 반환할 데이터가 없을때
        }
    }

    //Get ONE CONTENT,WORDS, SENTENES BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getContentById(@PathVariable("id") Long contentId) throws InterruptedException {
                    log.info("ContentId : {}",contentId);
        ContentDTO contentDTO = conSvc.getContentById(contentId);
                    log.info("contentDTO : {}",contentDTO.toString());
        List<MyWordDTO> myWordDTOList = wordSvc.getWordByContentId(contentId);

        Map<String, Object> response = new HashMap<>();
        if (contentDTO.getContentId()!=null) {
            response.put("contentDTO", contentDTO);
            response.put("myWordDTOList", myWordDTOList);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return ResponseEntity.noContent().build();  // 204 No Content 상태 반환, 요청은 성공했으나 반환할 데이터가 없을때
        }
    }
}
