package com.icetea.project.MonStu.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icetea.project.MonStu.domain.AiContent;
import com.icetea.project.MonStu.domain.Member;
import com.icetea.project.MonStu.domain.MyWord;
import com.icetea.project.MonStu.dto.TranslationDTO;
import com.icetea.project.MonStu.repository.AiContentRepository;
import com.icetea.project.MonStu.repository.MemberRepository;
import com.icetea.project.MonStu.repository.WordRepository;
import com.icetea.project.MonStu.util.AuthManager;
import com.icetea.project.MonStu.util.NaverAPIManager;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class TranslationService {

    private final WebClient webClient;  //Spring WebFlux에서 비동기식, 반응형 HTTP 요청을 수행하기 위한 인터페이스
    private final MemberRepository memberRepository;
    private final AuthManager authManager;

    // API 키와 URL을 외부 설정 파일에서 주입받습니다.
    @Value("${naver.api.client.id}")
    private String clientId;
    @Value("${naver.api.client.secret}")
    private String clientSecret;

    private static final String PAPAGO_URL = "https://naveropenapi.apigw.ntruss.com/nmt/v1/translation";

    //Spring WebFlux와 함께 사용되는 Reactor Netty 기반의 비동기 HTTP 클라이언트를 설정
    public TranslationService(AuthManager authManager,MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
        this.authManager = authManager;

        //TCP 연결에 대한 시간 제한(타임아웃)을 설정
        HttpClient httpClient = HttpClient.create().tcpConfiguration(tcpClient ->
                tcpClient.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)    //5000ms
                        .doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(5))   //doOnConnected->TCP 연결이 성공적으로 이루어진 후
                                .addHandlerLast(new WriteTimeoutHandler(5)))    //addHandlerLast->5초 동안 서버로부터 데이터를 읽지,전송하지 못하면 타임아웃이 발생
        );

        this.webClient = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    //PAPAGO TRANSLATION API
    public String translateText(TranslationDTO translationDTO) {
        if(translationDTO.getText().isEmpty()) {
            log.info("Text is empty");
            return null;
        }
        System.out.println(translationDTO.toString());
        //기본 사용법
//        return this.webClient.post()
//                .uri(PAPAGO_URL)
//                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                .header("X-NCP-APIGW-API-KEY-ID", clientId)
//                .header("X-NCP-APIGW-API-KEY", clientSecret)
//                .bodyValue(translationRequestDTO)
//                .retrieve() //서버로부터 응답을 받는 메소드
//                .bodyToMono(TranslationRequestDTO.class)   //응답 바디를 변환
//                .block(); // block()은 비동기 호출을 동기적으로 기다립니다.

        //커스텀 사용법
        ClientResponse response = this.webClient.post()
                .uri(PAPAGO_URL)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header("X-NCP-APIGW-API-KEY-ID", clientId)
                .header("X-NCP-APIGW-API-KEY", clientSecret)
                .bodyValue(translationDTO)
                .exchange() // exchange()를 사용하여 응답을 수동으로 처리
                .block(); // block()으로 동기 대기

        if (response.statusCode().is2xxSuccessful()) {
            String responseBody = response.bodyToMono(String.class).block();
            log.info("PAPAGO Translated Successfully - {}", responseBody.toString());
            return responseBody;
        } else {
            String errorBody = response.bodyToMono(String.class).block();
            log.info("PAPAGO API 호출 중 에러 발생: {}", errorBody);
            throw new RuntimeException("PAPAGO API 호출 중 에러 발생: " + errorBody);
        }
    }

    /* GET USER ENTITY */
    private Member getSecurityUserEntity(){
        return memberRepository.findByEmail(authManager.getUserName()).orElseThrow(()-> new UsernameNotFoundException("User not found"));
    }
}
