package com.icetea.project.MonStu.service;

import com.icetea.project.MonStu.domain.Content;
import com.icetea.project.MonStu.domain.MyWord;
import com.icetea.project.MonStu.dto.MyWordDTO;
import com.icetea.project.MonStu.repository.ContentRepository;
import com.icetea.project.MonStu.repository.MyWordRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class MyWordService {

    private final MyWordRepository myWordRps;
    private final ContentRepository contentRps;
    private final ModelMapper modelMapper;
    private final JPAQueryFactory queryFactory;

    public MyWordService(MyWordRepository myWordRps,ContentRepository contentRps, ModelMapper modelMapper, JPAQueryFactory queryFactory){
        this.myWordRps = myWordRps;
        this.contentRps = contentRps;
        this.modelMapper = modelMapper;
        this.queryFactory = queryFactory;
    }

    //DB에 저장되지 않은 단어만 저장
    @Transactional
    public Boolean saveWords(List<MyWordDTO> myWordDTOS){
        if (myWordDTOS.isEmpty()) {return null;}
        try{
            Long contentId = myWordDTOS.get(0).getContentId();
            Content content = contentRps.findById(contentId).orElseThrow(() -> new IllegalArgumentException("Invalid contentId: " + contentId));
            log.info("content : {}",content.toString());
            List<MyWord> myWords = myWordDTOS.stream()
                    .map(dto -> {
                        MyWord myWord = modelMapper.map(dto, MyWord.class);
                        log.info("myWord : {}",myWord.toString());
                        if(myWord.getMyWordId()==null){
                            myWord.setContent(content);
                            return myWord;
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
                    .toList();

            myWords.forEach(content::addMyWords);    //myWords.content에 addMyWords(myword)를 실행

            contentRps.save(content);
            log.info("saveWords Succesful");
            return true;
        }catch (Exception e){
            log.info("saveWords error - {}",e.getMessage());
            return false;
        }
    }

}
