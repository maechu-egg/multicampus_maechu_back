package com.multipjt.multi_pjt.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.multipjt.multi_pjt.user.dao.UserMapper;
import com.multipjt.multi_pjt.user.domain.UserResponseDTO;

import java.util.List;


@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

   
}
