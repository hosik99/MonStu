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
    private final NaverAPIManager naverAPIManager;
    private final WordRepository wordRepository;
    private final MemberRepository memberRepository;
    private final AiContentRepository aiContentRepository;
    private final AuthManager authManager;

    // API 키와 URL을 외부 설정 파일에서 주입받습니다.
    @Value("${naver.api.client.id}")
    private String clientId;
    @Value("${naver.api.client.secret}")
    private String clientSecret;

    @Value("${naver.api.studio.key}")
    private String studioKey;
    @Value("${naver.api.studio.gw.key}")
    private String studioGwKey;
    @Value("${naver.api.studio.request.id}")
    private String studioRequestId;

    private static final String PAPAGO_URL = "https://naveropenapi.apigw.ntruss.com/nmt/v1/translation";

    private static final String CLOVA_STUDIO_URL = "https://clovastudio.stream.ntruss.com/testapp/v1/chat-completions/HCX-DASH-001";

    //Spring WebFlux와 함께 사용되는 Reactor Netty 기반의 비동기 HTTP 클라이언트를 설정
    public TranslationService(WordRepository wordRepository,NaverAPIManager naverAPIManager,AiContentRepository aiContentRepository,AuthManager authManager,MemberRepository memberRepository) {
        this.wordRepository = wordRepository;
        this.memberRepository = memberRepository;
        this.naverAPIManager = naverAPIManager;
        this.aiContentRepository = aiContentRepository;
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

    //PAPAGO TRANSLATION API
    public String getContentByWords(Map<Long,List<Long>> wordIdList) {
        List wordList = getWordList(wordIdList);

        if(wordList.isEmpty()) {
            log.info("Word List is empty");
            return null;
        }
        Map<String, Object> requestMsg = naverAPIManager.requestToClova(wordList);

        ClientResponse response = this.webClient.post()
                .uri(CLOVA_STUDIO_URL)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header("X-NCP-CLOVASTUDIO-API-KEY", studioKey)
                .header("X-NCP-APIGW-API-KEY", studioGwKey)
                .header("X-NCP-CLOVASTUDIO-REQUEST-ID", studioRequestId)
//                .header("Accept", "text/event-stream")
                .bodyValue(requestMsg)
                .exchange() // exchange()를 사용하여 응답을 수동으로 처리
                .block(); // block()으로 동기 대기

        if (response.statusCode().is2xxSuccessful()) {
            String responseBody = response.bodyToMono(String.class).block();
            log.info("New Content Successfully generated - {}", responseBody.toString());
            saveAiContent(responseBody);    //aiContent DB에 저장
            return responseBody;
        } else {
            String errorBody = response.bodyToMono(String.class).block();
            log.info("CLOVA_STUDIO API 호출 중 에러 발생: {}", errorBody);
            throw new RuntimeException("CLOVA_STUDIO API 호출 중 에러 발생: " + errorBody);
        }
    }

    private List<String> getWordList(Map<Long,List<Long>> wordIdList){
        List<String> wordList = new ArrayList<>();
        List idList = new ArrayList<Long>();
        for(Long wordId : wordIdList.keySet()){
            idList = wordIdList.get(wordId);
            List<MyWord> entitiyList = wordRepository.findAllById(idList);
            for(MyWord entity : entitiyList){
                wordList.add(entity.getTargetWord());
            }
        }
        return wordList;
    }

    private void saveAiContent(String responseBody){
        AiContent aicon = new AiContent();

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            aicon.setAiContent( jsonNode.path("result").path("message").path("content").asText() );
        } catch (Exception e) {
            e.printStackTrace();
        }
        Member member = getSecurityUserEntity();
        aicon.setMember(member);
        member.addAiContent(aicon);
        memberRepository.save(member);
    }

    /* GET USER ENTITY */
    private Member getSecurityUserEntity(){
        return memberRepository.findByEmail(authManager.getUserName()).orElseThrow(()-> new UsernameNotFoundException("User not found"));
    }
}
