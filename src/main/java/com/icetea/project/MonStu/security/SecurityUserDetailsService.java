package com.icetea.project.MonStu.security;


import com.icetea.project.MonStu.domain.Member;
import com.icetea.project.MonStu.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class SecurityUserDetailsService implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Member> optional = memberRepo.findByEmail(email);
        if (optional.isEmpty()) {
            log.info(email+" 로그인 실패");
            throw new UsernameNotFoundException(email + " 사용자 없음");
        } else {
            Member member = optional.get();
            log.info(member.getEmail()+" 로그인 성공");
            return new SecurityUser(member);
        }
    }
}
