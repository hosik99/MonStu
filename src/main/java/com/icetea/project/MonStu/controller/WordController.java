package com.icetea.project.MonStu.controller;

import com.icetea.project.MonStu.dto.MyWordDTO;
import com.icetea.project.MonStu.service.WordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/member/word")
public class WordController {

    private final WordService wordSvc;

    public WordController(WordService wordSvc) {
        this.wordSvc = wordSvc;
    }

    //save MyWords
    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody List<MyWordDTO> myWordDTOList){
        Boolean saved = wordSvc.saveWords(myWordDTOList);
        return saved ?
                new ResponseEntity<>(HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.GATEWAY_TIMEOUT);
    }

    //get all MyWords by username
    @GetMapping("/my")
    public ResponseEntity<List<MyWordDTO>> getMyWords(){
        List<MyWordDTO> myWordDTOList = wordSvc.getAllMyWords();
        myWordDTOList.stream()
                .forEach(System.out::println);
        return new ResponseEntity<>(myWordDTOList, HttpStatus.OK);
    }

    //Delete MyWords by Id
    @PostMapping("/del")
    public ResponseEntity<String> delMyWords(@RequestBody Map<Long,List<MyWordDTO>> delList){
        wordSvc.delMyWords(delList);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
