package com.multipjt.multi_pjt.user.ctrl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.multipjt.multi_pjt.user.domain.UserResponseDTO;
import com.multipjt.multi_pjt.user.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/index")
    public ResponseEntity<Object>  test() {
        System.out.println("client endpoint : /user/index" + userService);
        List<UserResponseDTO> list = userService.findAll();
       System.out.println("result size = " + list.size());
        if( list.size() == 0) {
            Map<String, String> map = new HashMap<>();
            map.put("info", "저장된 데이터가 존재하지 않습니다. ");
            return new ResponseEntity<>(list, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }
    
}
