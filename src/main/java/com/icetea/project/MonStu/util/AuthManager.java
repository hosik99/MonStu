package com.icetea.project.MonStu.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/*
    @AuthenticationPrincipal로 대체 가능
*/
@Component
public class AuthManager {
    public AuthManager() {}

    //현재 인증된 사용자정보 가져오기
    /*
    인증되지 않았거나 익명 사용자인 경우, authentication.getPrincipal()은 기본적으로 anonymousUser 또는 null을 반환할 수 있으며, toString()은 anonymousUser를 반환할 수 있습니다.
    */
    public String getUserName(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {   //현재 인증된 사용자의 주체(Principal)
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            username = userDetails.getUsername();
        } else {
            username = authentication.getPrincipal().toString();    //anonymousUser 반환
        }
        return username;
    }

}
