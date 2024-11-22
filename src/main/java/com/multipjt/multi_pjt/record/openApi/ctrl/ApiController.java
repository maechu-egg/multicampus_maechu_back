package com.multipjt.multi_pjt.record.openApi.ctrl;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.multipjt.multi_pjt.jwt.JwtTokenProvider;
import com.multipjt.multi_pjt.record.openApi.domain.NutirientDTO;
import com.multipjt.multi_pjt.record.openApi.service.ApiService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("record/api")

public class ApiController {
    
    @Autowired
    private ApiService apiService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;


    @Value("${openApi.servicekey}")
    private String  serviceKey;

    @Value("${openApi.callBackUrl}")
    private String callBackUrl ;

    @Value("${openApi.dataType}")
    private String dataType ;

    @GetMapping("/nutrient")
    public ResponseEntity<Object> callNutrientApi(
        @Valid @RequestParam(name = "foodNm") String foodNm,
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) throws UnsupportedEncodingException{
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // "Bearer " 접두사 제거
            Integer member_id = jwtTokenProvider.getUserIdFromToken(token); // 토큰에서 사용자 ID 추출
    

            if(foodNm == null || foodNm.isEmpty()){
                return new ResponseEntity<>("식품명을 입력해주세요.",HttpStatus.BAD_REQUEST);
            } else{    
            String encodeFoodNm = URLEncoder.encode(foodNm, "UTF-8");    

            System.out.println("client Request path : /record/api/nutrient");
            System.out.println("serviceKey >>> " + serviceKey);
            System.out.println("callBackUrl >>> " + callBackUrl);   
            System.out.println("dataType >>> " + dataType);
            System.out.println("params >>" + encodeFoodNm);

            String pageNo = "1";
            String numOfRows = "100";

            String requestUrl = callBackUrl 
                                + "?serviceKey=" + serviceKey
                                +"&pageNo=" + pageNo
                                +"&numOfRows=" + numOfRows
                                + "&type=" + dataType 
                                + "&FOOD_NM_KR=" + encodeFoodNm;
                
            System.out.println("requestUrl >>> " + requestUrl);                            

                HttpURLConnection http = null;
                InputStream       stream = null;
                String            result = null;
                List<NutirientDTO> list = null;

                try{
                    http = (HttpURLConnection) new URL(requestUrl).openConnection();
                    System.out.println("http connection >>> " + http);
                    int responseCode = http.getResponseCode();
                    System.out.println("responseCode >>> " + responseCode);
                    

                    if(responseCode == HttpURLConnection.HTTP_OK){
                        stream = http.getInputStream();
                        result = StreamUtils.copyToString(stream, Charset.forName("UTF-8"));
                        list = apiService.parseJson(result);    
                    
                        System.out.println("api list >>> " + list);
                        if(list.isEmpty()){ 
                            return new ResponseEntity<>("해당 식품이 없습니다.",HttpStatus.NOT_FOUND);
                        } 
                    }else{ 
                        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                } finally{
                    if(stream != null){
                        try{
                            stream.close();
                        } catch(IOException e){
                            e.printStackTrace();
                        }
                    }
                }

            // 유사도 계산 후 상위 10개 식품 반환
                LevenshteinDistance levenshtein = new LevenshteinDistance();
                list.sort((a, b) -> {
                    int distanceA = levenshtein.apply(foodNm, a.getFoodNm());
                    int distanceB = levenshtein.apply(foodNm, b.getFoodNm());
                    return Integer.compare(distanceA, distanceB);
                });

                List<NutirientDTO> top10List = list.stream()
                                                .limit(30)
                                                .collect(Collectors.toList());


                return new ResponseEntity<>(top10List,HttpStatus.OK);
            }
        } else{
            return new ResponseEntity<>("인증실패",HttpStatus.UNAUTHORIZED);
        }
    }

// 식품 코드 이용한 필터링 -> 구현 중
//@GetMapping("/nutrient")
// public ResponseEntity<Object> callNutrientApi(
//     @Valid @RequestBody Map<String, String> foodInfo) throws UnsupportedEncodingException {
    
//         if (foodInfo.get("foodNm") == null || foodInfo.get("foodCD") == null) {
//             return new ResponseEntity<>("foodNm 또는 foodCD가 누락되었습니다.", HttpStatus.BAD_REQUEST);
//         }

//         String foodNm = foodInfo.get("foodNm");
//         String foodCD = foodInfo.get("foodCD");
            
            
//         String encodeFoodNm = URLEncoder.encode(foodNm, "UTF-8");    
//         //String encodeFoodCD = URLEncoder.encode(foodCD, "UTF-8");
        
//         System.out.println("client Request path : /record/api/nutrient");
//         System.out.println("keyId >>> " + keyId);
//         System.out.println("serviceId >>> " + serviceId);
//         System.out.println("callBackUrl >>> " + callBackUrl);   
//         System.out.println("dataType >>> " + dataType);
//         System.out.println("params1 >>" + encodeFoodNm);
//         System.out.println("params2 >>" + foodCD);
//         String startIdx = "1";
//         String endIdx = "5";

//         String requestUrl = callBackUrl 
//                             + keyId 
//                             + "/" + serviceId 
//                             + "/" + dataType 
//                             + "/" + startIdx
//                             + "/" + endIdx
//                             + "/DESC_KOR=" + encodeFoodNm.trim()
//                             + "&FOOD_CD=" + foodCD.trim();  // 공백 제거

//         System.out.println("requestUrl >>> " + requestUrl);                            

//         HttpURLConnection http = null;
//         InputStream       stream = null;
//         String            result = null;
//         List<NutirientDTO> list = null;

//         try{
//             http = (HttpURLConnection) new URL(requestUrl).openConnection();
//             System.out.println("http connection >>> " + http);
//             int responseCode = http.getResponseCode();
//             System.out.println("responseCode >>> " + responseCode);
            

//             if(responseCode == HttpURLConnection.HTTP_OK){
//             stream = http.getInputStream();
//             result = StreamUtils.copyToString(stream, Charset.forName("UTF-8"));
//             list = apiService.parseJson(result);    
            
//             System.out.println("api list >>> " + list);
            
//             } else{ 
//                 return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//             }
//         }catch(Exception e){
//             e.printStackTrace();
//         } finally{
//             if(stream != null){
//                 try{
//                     stream.close();
//                 } catch(IOException e){
//                     e.printStackTrace();
//                 }
//             }
//         }
//         return new ResponseEntity<>(list,HttpStatus.OK);
//     }
}
