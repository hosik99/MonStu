package com.icetea.project.MonStu.service;

import com.icetea.project.MonStu.domain.Member;
import com.icetea.project.MonStu.repository.MemberRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private MemberRepository memberRps;

    public MemberService(MemberRepository memberRps) {
        this.memberRps = memberRps;
    }

    public Member getSecurityUserEntity() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return memberRps.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
