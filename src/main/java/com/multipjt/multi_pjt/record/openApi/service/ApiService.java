package com.multipjt.multi_pjt.record.openApi.service;

import java.util.List;
import java.util.ArrayList;

import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.multipjt.multi_pjt.record.openApi.domain.NutirientDTO;
import com.multipjt.multi_pjt.record.openApi.domain.FoodCalculateDTO;

@Service
public class ApiService {

    public List<NutirientDTO> parseJson(String result) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<NutirientDTO> list = new ArrayList<>();
    
        try {
            JsonNode rootNode = objectMapper.readTree(result);
            JsonNode itemsNode = rootNode.path("body").path("items");
    
            if (itemsNode.isArray()) {
                for (JsonNode node : itemsNode) {
                    NutirientDTO dto = new NutirientDTO();
    
                    // 필드 매핑
                    dto.setFoodClass(node.path("DB_GRP_NM").asText());
                    dto.setFoodNm(node.path("FOOD_NM_KR").asText());
    
                    // 숫자 변환 시 예외 처리 및 기본값 설정
                    dto.setEnergy(parseDoubleSafely(node.path("AMT_NUM1").asText()));
                    dto.setCarbs(parseDoubleSafely(node.path("AMT_NUM7").asText()));
                    dto.setProtein(parseDoubleSafely(node.path("AMT_NUM3").asText()));
                    dto.setFat(parseDoubleSafely(node.path("AMT_NUM4").asText()));
                    dto.setSugar(parseDoubleSafely(node.path("AMT_NUM8").asText()));
                    dto.setNat(parseDoubleSafely(node.path("AMT_NUM13").asText()));
                    dto.setChole(parseDoubleSafely(node.path("AMT_NUM24").asText()));
                    dto.setFatsat(parseDoubleSafely(node.path("AMT_NUM25").asText()));
                    dto.setFatrn(parseDoubleSafely(node.path("AMT_NUM26").asText()));
                    dto.setCal(parseDoubleSafely(node.path("AMT_NUM10").asText()));
    
                    list.add(dto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // 안전하게 Double로 변환
    private Double parseDoubleSafely(String text) {
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return 0.0; // 기본값
        }
    }
    
    public FoodCalculateDTO convertToCalculateDTO(NutirientDTO nutrientDTO) {
        FoodCalculateDTO calcDTO = new FoodCalculateDTO();
        calcDTO.setFoodNm(nutrientDTO.getFoodNm());
        calcDTO.setEnergy(nutrientDTO.getEnergy());
        calcDTO.setCarbs(nutrientDTO.getCarbs());
        calcDTO.setProtein(nutrientDTO.getProtein());
        calcDTO.setFat(nutrientDTO.getFat());
        return calcDTO;
    }
}