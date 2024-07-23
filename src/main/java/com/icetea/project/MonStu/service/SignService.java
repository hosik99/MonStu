package com.icetea.project.MonStu.service;

import com.icetea.project.MonStu.domain.Member;
import com.icetea.project.MonStu.domain.MemberInfo;
import com.icetea.project.MonStu.dto.MemberDTO;
import com.icetea.project.MonStu.dto.MemberInfoDTO;
import com.icetea.project.MonStu.repository.MemberInfoRepository;
import com.icetea.project.MonStu.repository.MemberRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SignService {

    private final MemberRepository memberRps;
    private final MemberInfoRepository memberInfoRps;
    private final ModelMapper modelMapper;
    private PasswordEncoder pwEncoder;

    public SignService(MemberRepository memberRps, MemberInfoRepository memberInfoRps, ModelMapper modelMapper,PasswordEncoder pwEncoder) {
        this.modelMapper = modelMapper;
        this.memberRps = memberRps;
        this.memberInfoRps = memberInfoRps;
        this.pwEncoder = pwEncoder;
    }

    //유저 하나 저장
    public Boolean saveMember(MemberDTO memberDTO, MemberInfoDTO memberInfoDTO) {
        Member member = modelMapper.map(memberDTO, Member.class);
        MemberInfo memberInfo = modelMapper.map(memberInfoDTO, MemberInfo.class);

        String originalPassword = member.getMemberPw();
        member.setMemberPw(pwEncoder.encode(originalPassword));
        member.setMemberInfo(memberInfo);
        memberInfo.setMember(member);
        Member savedMember = memberRps.save(member);
        return savedMember.getMemberId() != null;
    }

}
