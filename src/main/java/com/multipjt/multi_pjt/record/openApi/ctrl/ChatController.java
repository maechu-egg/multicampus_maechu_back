package com.multipjt.multi_pjt.record.openApi.ctrl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Value("${openai.api.key}")
    private String apiKey;

    @PostMapping
    public String chat(@RequestBody String userInput) {
        RestTemplate restTemplate = new RestTemplate();
        // OpenAI API 호출 로직 추가
        String response = ""; // 응답 변수를 초기화
        // ... OpenAI API 호출 후 response에 값 할당
        return response; // OpenAI의 응답 반환
    }
}
