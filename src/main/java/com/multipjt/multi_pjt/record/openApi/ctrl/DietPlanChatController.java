package com.multipjt.multi_pjt.record.openApi.ctrl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/diet")
public class DietPlanChatController {

    @Value("${openai.api.key}")
    private String apiKey;

    @PostMapping("/generate")
    public Map<String, Object> generateDietPlan(@RequestBody Map<String, Object> request) {
        // 요청에서 필요한 데이터 추출
        String calories = (String) request.get("calories");
        String ingredients = (String) request.get("ingredients");
        List<String> dietaryRestrictions = (List<String>) request.get("dietary_restrictions");
        List<String> allergies = (List<String>) request.get("allergies");
        List<String> medicalConditions = (List<String>) request.get("medical_conditions");
        String mealsPerDay = (String) request.get("meals_per_day");
        List<String> cookingPreference = (List<String>) request.get("cooking_preference");

        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.openai.com/v1/chat/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        // 메시지 생성
        String userMessage = String.format(
            "Create a diet plan with the following requirements:\n" +
            "Calories: %s\n" +
            "Ingredients: %s\n" +
            "Dietary Restrictions: %s\n" +
            "Allergies: %s\n" +
            "Medical Conditions: %s\n" +
            "Meals per day: %s\n" +
            "Cooking Preference: %s",
            calories,
            ingredients,
            String.join(", ", dietaryRestrictions),
            String.join(", ", allergies),
            String.join(", ", medicalConditions),
            mealsPerDay,
            String.join(", ", cookingPreference)
        );

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-3.5-turbo");
        requestBody.put("messages", List.of(
                Map.of("role", "system", "content", "식단 계획을 마크다운 형식으로 작성하세요. 당신은 영양 전문가입니다."),
                Map.of("role", "user", "content", userMessage)
        ));

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Map.class);

        // OpenAI API 응답에서 실제 응답 텍스트 추출
        Map<String, Object> responseBody = responseEntity.getBody();
        List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
        Map<String, Object> firstChoice = choices.get(0);
        Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");
        String dietPlan = (String) message.get("content");

        // 응답 반환
        Map<String, Object> response = new HashMap<>();
        response.put("dietPlan", dietPlan);

        return response;
    }
}