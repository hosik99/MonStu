package com.icetea.project.MonStu.controller;

import com.icetea.project.MonStu.domain.AiContent;
import com.icetea.project.MonStu.dto.AiContentDTO;
import com.icetea.project.MonStu.enums.ResponseMsg;
import com.icetea.project.MonStu.service.AiContentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/member/aicon")
public class AiContentController {

    private final AiContentService aiConSvc;

    public AiContentController(AiContentService aiConSvc) {
        this.aiConSvc = aiConSvc;
    }

    @GetMapping("/getAll")
    public ResponseEntity<Map<String, Object>> getAllByMemberId() {
        List<AiContentDTO> aiContentList= aiConSvc.getAllByMemberId();
        Map<String, Object> response = new HashMap<>();
        if (!aiContentList.isEmpty()) {
            response.put("aiContentDTOList", aiContentList);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return ResponseEntity.noContent().build();  // 204 No Content 상태 반환, 요청은 성공했으나 반환할 데이터가 없을때
        }
    }

    @PostMapping("/del")
    public ResponseEntity<String> delById(@RequestBody Long id) {
        System.out.println("delById: "+id);
        aiConSvc.delById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //Create New Content (used CLOVA STUDIO AI)
    @PostMapping("/api/con")
    public ResponseEntity<Map<String, Object>> getContentApi(@RequestBody Map<Long,List<Long>> wordIdList){
        Map<String, Object> response = new HashMap<>();
        log.info(wordIdList.toString());

        response.put("apiResponse",aiConSvc.getContentByWords(wordIdList));
        response.put("stateMessage", ResponseMsg.CREATED_SUCCESS.getMessage());
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }
}
