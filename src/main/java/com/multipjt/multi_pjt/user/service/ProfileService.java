package com.multipjt.multi_pjt.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.multipjt.multi_pjt.user.ctrl.ProfileController;
import com.multipjt.multi_pjt.user.dao.ProfileMapper;
import com.multipjt.multi_pjt.user.domain.mypage.ProfileRequestDTO;
import com.multipjt.multi_pjt.user.domain.mypage.ProfileResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ProfileService {
    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);
    
    @Autowired
    private ProfileMapper profileMapper;

    public ResponseEntity<String> registerProfile(ProfileRequestDTO profileRequestDTO, int member_id) {
        profileRequestDTO.setMemberId(member_id);
        
        // 중복 확인
        ProfileResponseDTO existingProfile = profileMapper.getUserById(member_id);
        if (existingProfile != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("{\"Code\": \"DUPLICATE_MEMBER_ID\", \"Message\": \"이미 존재하는 member_id입니다.\"}");
        }

        logger.info("profileRequestDTO-getMemberId: {}", profileRequestDTO.getMemberId());
        try {
            profileMapper.registerProfile(profileRequestDTO);
            return ResponseEntity.ok("{\"Code\": \"SUCCESS\", \"Message\": \"정상적으로 프로필이 등록되었습니다.\"}");
        } catch (Exception e) {
            logger.error("Error registering profile", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("{\"Code\": \"Internal Server Error\", \"Message\": \"프로필 등록에 실패했습니다.\"}");
        }
    }

    public ResponseEntity<String> updateProfile(int member_id, ProfileRequestDTO profileRequestDTO) {
        profileRequestDTO.setMemberId(member_id);
        logger.info("profileRequestDTO-getMemberId: {}", profileRequestDTO.getMemberId());
       
        try {
            profileMapper.updateProfile(profileRequestDTO);
            return ResponseEntity.ok("{\"Code\": \"SUCCESS\", \"Message\": \"정상적으로 프로필이 수정되었습니다.\"}");
        } catch (Exception e) {
            logger.error("Error registering profile", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("{\"Code\": \"Internal Server Error\", \"Message\": \"프로필 수정에 실패했습니다.\"}");
        }
    }

    public ProfileResponseDTO getProfile(int member_id) {
        ProfileResponseDTO profile = profileMapper.getUserById(member_id); 
        if (profile != null) {
            logger.info("User info retrieved successfully for userId: {}", member_id);
            logger.info("User info retrieved successfully for profileUser: {}", profile);
        } else {
            logger.warn("User not found for userId: {}", member_id);
        }
        return profile;
    }
}
