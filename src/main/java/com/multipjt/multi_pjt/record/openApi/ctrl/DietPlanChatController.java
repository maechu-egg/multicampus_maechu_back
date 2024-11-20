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

import com.multipjt.multi_pjt.record.openApi.domain.dietplan.DietPlanChatRequestDTO;
import com.multipjt.multi_pjt.record.openApi.domain.dietplan.DietPlanChatResponseDTO;

@RestController
@RequestMapping("/diet")
public class DietPlanChatController {

    @Value("${gemini.api.key}")
    private String apiKey;

    @PostMapping("/generate")
    public DietPlanChatResponseDTO generateDietPlan(@RequestBody DietPlanChatRequestDTO request) {
        // 요청에서 필요한 데이터 추출
        int calories = request.getCalories();
        String exercisegoal = request.getExercisegoal();
        String ingredients = request.getIngredients();
        List<String> dietaryRestrictions = request.getDietaryRestrictions() != null ? request.getDietaryRestrictions() : List.of();
        List<String> allergies = request.getAllergies() != null ? request.getAllergies() : List.of();
        List<String> medicalConditions = request.getMedicalConditions() != null ? request.getMedicalConditions() : List.of();
        String mealsPerDay = request.getMealsPerDay();
        List<String> cookingPreference = request.getCookingPreference() != null ? request.getCookingPreference() : List.of();
        
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-goog-api-key", apiKey);
        
        // 메시지 생성
        String userMessage = String.format(
            "식단 계획을 마크다운 형식으로 작성하세요. 당신은 영양 전문가입니다. 사용자가 지정한 식단 요구 사항을 충족시키는 식단 계획을 개발하는 것이 당신의 임무입니다. 답변은 상세하고 구조화되며 유익해야 하며, 식단 계획을 제시하는 데 마크다운 표를 사용해야 합니다. 사용자의 칼로리 목표, 선호 재료, 식이 제한, 하루 식사 횟수를 고려하세요. 각 식사에 대한 분석을 제공하며, 칼로리 함량 및 주요 영양소와 같은 영양 정보를 포함시키세요.\n" +
            "다음 요구 사항을 충족하는 식단 계획을 만드세요:\n" +
            "칼로리: %s (하루 목표 칼로리 수)\n" +
            "식단 목표: %s (운동목표)\n" +
            "재료: %s (식단을 구성하는 재료)\n" +
            "식이 제한: %s (제한하고 싶은 식품군)\n" +
            "알레르기: %s (알레르기 및 불내증)\n" +
            "의료 조건: %s (당신이 앓고 있는 질병 또는 의학적 조건)\n" +
            "하루 식사 횟수: %s (하루에 먹고 싶은 식사 수)\n" +
            "조리 난이도: %s (조리 난이도)\n\n" +
            "식단 계획은 마크다운 형식으로 작성되어야 하며, 각 식사에 대한 분석을 제공하고, 칼로리 함량 및 주요 영양소와 같은 영양 정보를 포함해야 합니다. 아래는 예시 형식입니다:\n\n" +
            "### 식단 계획 예시\n" +
            "| 식사    | 음식    | 양(g)    | 열량 (kcal) | 영양소 정보 |\n" +
            "|---------|---------|---------|-------------|------------|\n" +
            "| 아침 식사 | 스크램블 에그와 야채 | 20g, 야채 추가 | 300kal | 단백질 20g, 지방 10g, 탄수화물 10g |\n" +
            "| 점심 식사 | 구운 닭고기 샐러드 | 150g, 다양한 채소 | 400kal | 단백질 30g, 지방 15g, 탄수화물 20g |\n" +
            "| 저녁 식사 | 구운 연어와 현미 | 150g, 1컵 | 500kal | 단백질 25g, 지방 10g, 탄수화물 30g |\n" +
            "| 간식     | 과일 | 1개 | 100kal | 단백질 1g, 지방 0g, 탄수화물 20g |\n\n" +
            "**합계**\n" +
            "- 열량: 약 2200 kcal\n" +
            "- 단백질: 100~120g (변동 가능)\n\n" +
            "### **주의 사항:**(식단의 주의사항)\n" +
            "- 이 식단 계획은 일반적인 지침이며, 개인적인 요구 사항에 따라 조정될 수 있습니다.\n\n" +
            "- 식단 계획은 칼로리 함량 및 주요 영양소와 같은 영양 정보를 포함해야 합니다.\n\n",
            calories,
            exercisegoal,
            ingredients,
            String.join(", ", dietaryRestrictions),
            String.join(", ", allergies),
            String.join(", ", medicalConditions),
            mealsPerDay,
            String.join(", ", cookingPreference)
        );
        // 요청 본문 생성   
        Map<String, Object> requestBody = new HashMap<>();
        Map<String, Object> contents = new HashMap<>();
        contents.put("parts", List.of(Map.of("text", userMessage)));
        
        requestBody.put("contents", List.of(contents));

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Map.class);
        
        // Gemini API 응답에서 실제 응답 텍스트 추출
        Map<String, Object> responseBody = responseEntity.getBody();
        List<Map<String, Object>> candidates = (List<Map<String, Object>>) responseBody.get("candidates");
        Map<String, Object> firstCandidate = candidates.get(0);
        Map<String, Object> content = (Map<String, Object>) firstCandidate.get("content");
        List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
        String dietPlan = (String) parts.get(0).get("text");
        
        // 응답 DTO 생성
        DietPlanChatResponseDTO response = new DietPlanChatResponseDTO();
        response.setCalories(calories);
        response.setExercisegoal(exercisegoal);
        response.setIngredients(ingredients);
        response.setDietaryRestrictions(dietaryRestrictions);
        response.setAllergies(allergies);
        response.setMedicalConditions(medicalConditions);
        response.setMealsPerDay(mealsPerDay);
        response.setCookingPreference(cookingPreference);
        response.setDietPlan(dietPlan); 

        // 채팅 형식의 메시지 생성
        List<Map<String, Object>> messages = List.of(
            Map.of("role", "user", "content", userMessage),
            Map.of("role", "assistant", "content", dietPlan)
        );

        // 메시지를 응답 DTO에 추가
        response.setMessages(messages);

        return response;
    }
}