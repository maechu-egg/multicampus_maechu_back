package com.multipjt.multi_pjt.record.openApi.service;

import java.util.List;
import java.util.ArrayList;

import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.multipjt.multi_pjt.record.openApi.domain.NutirientDTO;

@Service
public class ApiService {
    
    public List<NutirientDTO> parseJson(String result) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<NutirientDTO> list = new ArrayList<>();

        try {
            JsonNode rootNode = objectMapper.readTree(result);
            JsonNode rowNode = rootNode.path("I2790").path("row");
            
            if (rowNode.isArray()) {
                for (JsonNode node : rowNode) {
                    NutirientDTO dto = new NutirientDTO();
                    dto.setFoodClass(node.path("GROUP_NAME").asText());
                    dto.setFoodNm(node.path("DESC_KOR").asText());
                    dto.setEnergy(node.path("NUTR_CONT1").asText());
                    dto.setQuantity(node.path("SERVING_SIZE").asText());
                    dto.setCarbs(node.path("NUTR_CONT2").asText());
                    dto.setProtein(node.path("NUTR_CONT3").asText());
                    dto.setFat(node.path("NUTR_CONT4").asText());
                    dto.setSugar(node.path("NUTR_CONT5").asText());
                    dto.setNat(node.path("NUTR_CONT6").asText());
                    dto.setChole(node.path("NUTR_CONT7").asText());
                    dto.setFatsat(node.path("NUTR_CONT8").asText());
                    dto.setFatrn(node.path("NUTR_CONT9").asText());
                    
                    list.add(dto);
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return list;
    }       
}
