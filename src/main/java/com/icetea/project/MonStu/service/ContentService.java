package com.icetea.project.MonStu.service;

import com.icetea.project.MonStu.domain.Content;
import com.icetea.project.MonStu.domain.Member;
import com.icetea.project.MonStu.dto.ContentDTO;
import com.icetea.project.MonStu.repository.ContentRepository;
import com.icetea.project.MonStu.repository.MemberRepository;
import com.icetea.project.MonStu.util.AuthManager;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ContentService {

    private final ContentRepository contentRps;
    private final MemberRepository memberRps;
    private final ModelMapper modelMapper;
    private final AuthManager authManager;

    public ContentService(ContentRepository contentRps, MemberRepository memberRps, ModelMapper modelMapper,AuthManager authManager) {
        this.contentRps = contentRps;
        this.memberRps = memberRps;
        this.modelMapper = modelMapper;
        this.authManager = authManager;
    }

    //ADD CONTENT BY USER's EMAIL
    public Boolean saveContent(ContentDTO contentDTO){
        Member member = getSecurityUserEntity();
        Content content = modelMapper.map(contentDTO, Content.class);

        member.addContent(content);
        content.setMember(member);
        memberRps.save(member);  //(CascadeType.ALL로 인해
        log.info("{} is Saved : {}",content.getTitle(),content.getContentId() != null);
        return content.getContentId() != null;
    }

    //GET ALL TITLES BY USER's EMAIL
    public List<ContentDTO> getTitlesByEmail(){
        Member member = getSecurityUserEntity();
        List<Content> contentList = contentRps.findByMember(member);

        List<ContentDTO> contentDTOList = contentList.stream()
                .map(content -> modelMapper.map(content, ContentDTO.class))
                .collect(Collectors.toList());
        return contentDTOList;
    }

    /* GET USER ENTITY */
    public Member getSecurityUserEntity(){
//        Member member = new Member();
//        LocalTime localTime = LocalTime.now();
//        int nowSeconds = localTime.getSecond();
//
//        if(nowSeconds%2 == 1){
//            member.setEmail("dong@na.com");
//        }else{
//            member.setEmail("yang@na.com");
//        }
//        log.info("nowSeconds: {} __ email: {}", nowSeconds, member.getEmail());
//        return member;
        return memberRps.findByEmail(authManager.getUserName()).orElseThrow(()-> new UsernameNotFoundException("User not found"));
    }

}
