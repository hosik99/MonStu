package com.icetea.project.MonStu.service;

import com.icetea.project.MonStu.domain.Content;
import com.icetea.project.MonStu.domain.Member;
import com.icetea.project.MonStu.dto.ContentDTO;
import com.icetea.project.MonStu.dto.MyWordDTO;
import com.icetea.project.MonStu.dto.QContentDTO;
import com.icetea.project.MonStu.dto.QMyWordDTO;
import com.icetea.project.MonStu.repository.ContentRepository;
import com.icetea.project.MonStu.repository.MemberRepository;
import com.icetea.project.MonStu.util.AuthManager;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.icetea.project.MonStu.domain.QContent.content1;
import static com.icetea.project.MonStu.domain.QMember.member;
import static com.icetea.project.MonStu.domain.QMyWord.myWord;

@Slf4j
@Service
public class ContentService {

    private final ContentRepository contentRps;
    private final MemberRepository memberRps;
    private final ModelMapper modelMapper;
    private final AuthManager authManager;
    private final JPAQueryFactory queryFactory;

    public ContentService(ContentRepository contentRps, MemberRepository memberRps, ModelMapper modelMapper,AuthManager authManager,JPAQueryFactory queryFactory) {
        this.contentRps = contentRps;
        this.memberRps = memberRps;
        this.modelMapper = modelMapper;
        this.authManager = authManager;
        this.queryFactory = queryFactory;
    }

    //ADD CONTENT BY USER's EMAIL
    @Transactional
    public Boolean saveContent(ContentDTO contentDTO){
        Member member = getSecurityUserEntity();

        Content content = modelMapper.map(contentDTO, Content.class);
        content.setMember(member);

        Content savedContent = contentRps.save(content);
        return savedContent.getContentId() != null;
    }

    //GET ALL TITLES BY USER's EMAIL
    public List<ContentDTO> getContentsByEmail(){

        Member securityMember = getSecurityUserEntity();
        return queryFactory
                .select(new QContentDTO(
                        content1.contentId,
                        content1.title,
                        content1.content,
                        content1.createdAt,
                        member.email
                ))
                .from(content1)
                .leftJoin(content1.member,member)
                .where(member.email.eq(securityMember.getEmail()))
                .fetch();

    }

    public ContentDTO getContentById(Long contentId) {
        Member securityMember = getSecurityUserEntity();
        return queryFactory
                .select(new QContentDTO(
                        content1.contentId,
                        content1.title,
                        content1.content,
                        content1.createdAt,
                        member.email
                ))
                .from(content1)
                .leftJoin(content1.member,member)
                .where(member.email.eq(securityMember.getEmail()))
                .where(content1.contentId.eq(contentId))
                .fetchOne();
    }

    /* GET USER ENTITY */
    public Member getSecurityUserEntity(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("JWT-email: "+email);
        return memberRps.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

}
