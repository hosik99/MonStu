package com.icetea.project.MonStu.util;

import com.icetea.project.MonStu.dto.MyWordDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class NaverAPIManager {

    //단어 목록을 제공하여 새로운 문장 생성
    public Map<String, Object> requestToClova(List<String> wordList){
        StringBuilder requestMsg = new StringBuilder();
        wordList.forEach(word -> requestMsg.append(word).append(","));
        requestMsg.append("\n");
        requestMsg.append("단어들을 사용해서 영어문장 하나를 만들어줘.");

        Map<String, Object> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", requestMsg.toString());

        List<Map<String, Object>> messages = new ArrayList<>();
        messages.add(userMessage);

        Map<String, Object> requestMessage = new HashMap<>();
        requestMessage.put("messages", messages);

        return requestMessage;
    }

}
