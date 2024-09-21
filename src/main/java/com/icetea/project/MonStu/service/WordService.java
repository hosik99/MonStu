package com.icetea.project.MonStu.service;

import com.icetea.project.MonStu.domain.Content;
import com.icetea.project.MonStu.domain.Member;
import com.icetea.project.MonStu.domain.MyWord;
import com.icetea.project.MonStu.domain.QMyWord;
import com.icetea.project.MonStu.dto.MyWordDTO;
import com.icetea.project.MonStu.dto.QMyWordDTO;
import com.icetea.project.MonStu.repository.ContentRepository;
import com.icetea.project.MonStu.repository.MemberRepository;
import com.icetea.project.MonStu.repository.WordRepository;
import com.icetea.project.MonStu.util.AuthManager;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.icetea.project.MonStu.domain.QContent.content1;
import static com.icetea.project.MonStu.domain.QMyWord.myWord;


@Slf4j
@Service
public class WordService {

    private final WordRepository wordRps;
    private final ContentRepository contentRps;
    private final MemberRepository memberRps;
    private final AuthManager authManager;
    private final ModelMapper modelMapper;
    private final JPAQueryFactory queryFactory;

    public WordService(WordRepository wordRps, ContentRepository contentRps, MemberRepository memberRps, AuthManager authManager, ModelMapper modelMapper, JPAQueryFactory queryFactory){
        this.wordRps = wordRps;
        this.contentRps = contentRps;
        this.memberRps = memberRps;
        this.authManager = authManager;
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

    public List<MyWordDTO> getWordByContentId(Long contentId){
        return queryFactory
                .select(new QMyWordDTO(
                        myWord.myWordId,
                        myWord.targetWord,
                        myWord.translatedWord,
                        myWord.content.contentId
                ))
                .from(myWord)
                .where(myWord.content.contentId.eq(contentId))
                .fetch();
    }

    public List<MyWordDTO> getAllMyWords() {
        Member memberEntity = getSecurityUserEntity();
        return queryFactory
                .select(new QMyWordDTO(
                    myWord.myWordId,
                    myWord.targetWord,
                    myWord.translatedWord,
                    content1.contentId,
                    content1.title
                ))
                .from(myWord)
                .join(myWord.content, content1)
                .where(content1.member.memberId.eq(memberEntity.getMemberId()))
                .fetch();
    }

    /* GET USER ENTITY */
    public Member getSecurityUserEntity(){
        return memberRps.findByEmail(authManager.getUserName()).orElseThrow(()-> new UsernameNotFoundException("User not found"));
    }

    @Transactional
    public Boolean delMyWords(Map<Long, List<Long>> delList) {
        try {
            for(Map.Entry<Long, List<Long>> entry : delList.entrySet()){
                Optional<Content> contentOpt = contentRps.findById(entry.getKey());
                Content contentEntity = contentOpt.orElse(null);
                List<MyWord> wordEntitiies = wordRps.findAllById(entry.getValue());
                wordEntitiies.forEach(word ->
                        contentEntity.removeMyWords(word)
                );

                contentRps.save(contentEntity);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
