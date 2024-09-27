package com.icetea.project.MonStu.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icetea.project.MonStu.domain.AiContent;
import com.icetea.project.MonStu.domain.Member;
import com.icetea.project.MonStu.domain.MyWord;
import com.icetea.project.MonStu.domain.QAiContent;
import com.icetea.project.MonStu.dto.AiContentDTO;
import com.icetea.project.MonStu.dto.QAiContentDTO;
import com.icetea.project.MonStu.dto.QContentDTO;
import com.icetea.project.MonStu.repository.AiContentRepository;
import com.icetea.project.MonStu.repository.MemberRepository;
import com.icetea.project.MonStu.repository.WordRepository;
import com.icetea.project.MonStu.util.AuthManager;
import com.icetea.project.MonStu.util.NaverAPIManager;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.icetea.project.MonStu.domain.QAiContent.aiContent1;
import static com.icetea.project.MonStu.domain.QContent.content1;
import static com.icetea.project.MonStu.domain.QMember.member;

@Slf4j
@Service
public class AiContentService {

    private final WebClient webClient;
    private final AiContentRepository aiContentRepository;
    private final MemberRepository memberRps;
    private final WordRepository wordRepository;
    private final NaverAPIManager naverAPIManager;
    private final ModelMapper modelMapper;
    private final AuthManager authManager;
    private final JPAQueryFactory queryFactory;

    @Value("${naver.api.studio.key}")
    private String studioKey;
    @Value("${naver.api.studio.gw.key}")
    private String studioGwKey;
    @Value("${naver.api.studio.request.id}")
    private String studioRequestId;

    private static final String CLOVA_STUDIO_URL = "https://clovastudio.stream.ntruss.com/testapp/v1/chat-completions/HCX-DASH-001";

    public AiContentService(AiContentRepository aiContentRepository,MemberRepository memberRps,ModelMapper modelMapper,AuthManager authManager,JPAQueryFactory queryFactory,NaverAPIManager naverAPIManager,WordRepository wordRepository){
        this.aiContentRepository = aiContentRepository;
        this.memberRps = memberRps;
        this.modelMapper = modelMapper;
        this.authManager = authManager;
        this.queryFactory = queryFactory;
        this.naverAPIManager = naverAPIManager;
        this.wordRepository = wordRepository;

        HttpClient httpClient = HttpClient.create().tcpConfiguration(tcpClient ->
                tcpClient.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)    //5000ms
                        .doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(5))   //doOnConnected->TCP 연결이 성공적으로 이루어진 후
                                .addHandlerLast(new WriteTimeoutHandler(5)))    //addHandlerLast->5초 동안 서버로부터 데이터를 읽지,전송하지 못하면 타임아웃이 발생
        );

        this.webClient = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
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

    //CREATE NEW AI CONTENT BY WORDLIST
    public String getContentByWords(Map<Long,List<Long>> wordIdList) {
        List wordList = getWordList(wordIdList);

        if (wordList.isEmpty()) {
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
        memberRps.save(member);
    }
}
