package com.icetea.project.MonStu.service;

import com.icetea.project.MonStu.domain.AiContent;
import com.icetea.project.MonStu.domain.Member;
import com.icetea.project.MonStu.domain.QAiContent;
import com.icetea.project.MonStu.dto.AiContentDTO;
import com.icetea.project.MonStu.dto.QAiContentDTO;
import com.icetea.project.MonStu.dto.QContentDTO;
import com.icetea.project.MonStu.repository.AiContentRepository;
import com.icetea.project.MonStu.repository.MemberRepository;
import com.icetea.project.MonStu.util.AuthManager;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.icetea.project.MonStu.domain.QAiContent.aiContent1;
import static com.icetea.project.MonStu.domain.QContent.content1;
import static com.icetea.project.MonStu.domain.QMember.member;

@Service
public class AiContentService {

    private static final Logger log = LoggerFactory.getLogger(AiContentService.class);
    private final AiContentRepository aiContentRepository;
    private final MemberRepository memberRps;
    private final ModelMapper modelMapper;
    private final AuthManager authManager;
    private final JPAQueryFactory queryFactory;

    public AiContentService(AiContentRepository aiContentRepository,MemberRepository memberRps,ModelMapper modelMapper,AuthManager authManager,JPAQueryFactory queryFactory){
        this.aiContentRepository = aiContentRepository;
        this.memberRps = memberRps;
        this.modelMapper = modelMapper;
        this.authManager = authManager;
        this.queryFactory = queryFactory;
    }

    public List<AiContentDTO> getAllByMemberId() {
        Member memberEntity = getSecurityUserEntity();

        return queryFactory
                .select(new QAiContentDTO(
                        aiContent1.aiContentId,
                        aiContent1.aiContent,
                        aiContent1.createdAt,
                        member.email
                ))
                .from(aiContent1)
                .join(aiContent1.member,member)
                .where(aiContent1.member.memberId.eq(memberEntity.getMemberId()))
                .fetch();
    }

    @Transactional
    public void delById(Long id) {
        Member memberEntity = getSecurityUserEntity();
        log.info("member-delById:{}",memberEntity.getAiContents());

        AiContent aiContentEntity = aiContentRepository.findById(id).orElseThrow(()-> new RuntimeException ("User not found"));
        log.info("aiContent-delById:{}",aiContentEntity);

        // AiContent가 Member의 aiContents에 포함되어 있는지 확인
        if (!memberEntity.getAiContents().contains(aiContentEntity)) {
            throw new IllegalArgumentException("AiContent does not belong to the member.");
        }

        memberEntity.removeAiContent(aiContentEntity);

        memberRps.save(memberEntity);
    }

    /* GET USER ENTITY */
    public Member getSecurityUserEntity(){
        String userName = authManager.getUserName();
        log.info("userName: "+ userName);
        return memberRps.findByEmail(userName).orElseThrow(()-> new UsernameNotFoundException("User not found"));
    }

}
