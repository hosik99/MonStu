package com.icetea.project.MonStu.service;

import com.icetea.project.MonStu.domain.Content;
import com.icetea.project.MonStu.domain.Member;
import com.icetea.project.MonStu.dto.ContentDTO;
import com.icetea.project.MonStu.repository.ContentRepository;
import com.icetea.project.MonStu.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Slf4j
@Service
public class ContentService {

    private final ContentRepository conRps;
    private final MemberRepository memberRps;
    private final ModelMapper modelMapper;

    public ContentService(ContentRepository conRps, MemberRepository memberRps, ModelMapper modelMapper) {
        this.conRps = conRps;
        this.memberRps = memberRps;
        this.modelMapper = modelMapper;
    }

    public Boolean saveContent(ContentDTO contentDTO){
        Member member = getSecurityUserEntity();
        Content content = modelMapper.map(contentDTO, Content.class);

        member.addContent(content);
        content.setMember(member);
        memberRps.save(member);  //(CascadeType.ALL로 인해
        log.info("{} is Saved : {}",content.getTitle(),content.getContentId() != null);
        return content.getContentId() != null;
    }

    /* etc,Method */
    public Member getSecurityUserEntity(){
        Member member = new Member();
        LocalTime localTime = LocalTime.now();
        int nowSeconds = localTime.getSecond();

        if(nowSeconds%2 == 1){
            member.setEmail("dong@na.com");
        }else{
            member.setEmail("yang@na.com");
        }
        log.info("nowSeconds: {} __ email: {}", nowSeconds, member.getEmail());
        return member;
//        return usersRps.findById(authManager.getUserName()).orElseThrow(()-> new UsernameNotFoundException("User not found"));
    }

}
