package com.multipjt.multi_pjt.record.openApi.domain;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class NumericStringDeserializer extends JsonDeserializer<Integer> {
    @Override
    public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String text = p.getText();
        if (text != null) {
            // 숫자 부분만 추출
            text = text.replaceAll("[^\\d]", ""); 
            if (!text.isEmpty()) {
                return Integer.parseInt(text);
            }
        }
        return 0; // 기본값
    }
}
