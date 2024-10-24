package com.multipjt.multi_pjt.record.openApi.service;

import java.util.List;

import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.multipjt.multi_pjt.record.openApi.domain.NutirientDTO;

@Service
public class ApiService {
    
    public List<NutirientDTO> parseJson(String result) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<NutirientDTO> list = null;

        try{
            list = objectMapper.readValue(result, new TypeReference<List<NutirientDTO>>() {});
        } catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }       
}
