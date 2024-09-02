package com.icetea.project.MonStu.controller;

import com.icetea.project.MonStu.dto.ContentDTO;
import com.icetea.project.MonStu.dto.MyWordDTO;
import com.icetea.project.MonStu.enums.ResponseMsg;
import com.icetea.project.MonStu.service.ContentService;
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

    private ContentService conSvc;

    public ContentController(ContentService conSvc) { this.conSvc = conSvc; }

    @PostMapping("/add")
    public ResponseEntity<String> saveContent(@RequestBody ContentDTO contentDTO){
        log.info("ContentDTO : {}",contentDTO.toString());
        Boolean isSaved = conSvc.saveContent(contentDTO);
        return isSaved ?
                new ResponseEntity<>(ResponseMsg.SAVE_SUCCESS.getMessage(),HttpStatus.OK):
                new ResponseEntity<>(ResponseMsg.SAVE_FAILURE.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
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

        List<MyWordDTO> myWordDTOList = conSvc.getWordByContentId(contentId);

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
