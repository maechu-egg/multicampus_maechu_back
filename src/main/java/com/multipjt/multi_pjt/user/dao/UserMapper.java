package com.multipjt.multi_pjt.user.dao;

import org.apache.ibatis.annotations.Mapper;

import com.multipjt.multi_pjt.user.domain.UserResponseDTO;

import java.util.List;


@Mapper
public interface UserMapper {
    public List<UserResponseDTO> findAllRow();
} 