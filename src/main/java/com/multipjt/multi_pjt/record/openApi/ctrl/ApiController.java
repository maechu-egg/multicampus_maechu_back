package com.multipjt.multi_pjt.record.openApi.ctrl;

import java.io.IOException;
import java.io.InputStream;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.multipjt.multi_pjt.record.openApi.domain.NutirientDTO;
import com.multipjt.multi_pjt.record.openApi.service.ApiService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/record/api")

public class ApiController {
    
    @Autowired
    private ApiService apiService;

    @Value("${openApi.keyId}")
    private String keyId;

    @Value("${openApi.serviceId}")
    private String  serviceId;

    @Value("${openApi.callBackUrl}")
    private String callBackUrl ;

    @Value("${openApi.dataType}")
    private String dataType ;

    @GetMapping("/nutrient")
    public ResponseEntity<Object> callNutrientApi(
        @Valid @RequestParam(name = "foodNm") String foodNm) throws UnsupportedEncodingException{
        
        if(foodNm == null || foodNm.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else{    
        String encodeFoodNm = URLEncoder.encode(foodNm, "UTF-8");    

        System.out.println("client Request path : /record/api/nutrient");
        System.out.println("keyId >>> " + keyId);
        System.out.println("serviceId >>> " + serviceId);
        System.out.println("callBackUrl >>> " + callBackUrl);   
        System.out.println("dataType >>> " + dataType);
        System.out.println("params >>" + encodeFoodNm);

        String startIdx = "1";
        String endIdx = "10";

        String requestUrl = callBackUrl 
                            + keyId 
                            + "/" + serviceId 
                            + "/" + dataType 
                            + "/" + startIdx
                            + "/" + endIdx
                            + "/DESC_KOR=" + encodeFoodNm;
            
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
                
                } else{ 
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
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
            return new ResponseEntity<>(list,HttpStatus.OK);
        }
    }
}
