package com.multipjt.multi_pjt.user;

import static org.assertj.core.api.Assertions.assertThat;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.multipjt.multi_pjt.user.dao.ProfileMapper;
import com.multipjt.multi_pjt.user.dao.UserMapper;
import com.multipjt.multi_pjt.user.domain.UserRequestDTO;
import com.multipjt.multi_pjt.user.domain.mypage.ProfileRequestDTO;
import com.multipjt.multi_pjt.user.domain.mypage.ProfileResponseDTO;

@SpringBootTest
public class MyPageMapperApplicationTests {

    @Autowired
    private ProfileMapper profileMapper; 

    @Autowired
    private UserMapper userMapper;

    @Test
    @DisplayName("015 : 사용자 별 프로필 등록")
    public void testRegisterProfile() {
        // given : 프로필 등록을 위한 DTO 설정
        ProfileRequestDTO profileRequest = new ProfileRequestDTO();
        profileRequest.setProfile_id("P003");
        profileRequest.setProfile_gender("M");
        profileRequest.setProfile_age(25);
        profileRequest.setProfile_region("Seoul");
        profileRequest.setProfile_weight(70.5f);
        profileRequest.setProfile_height(175.0f);
        profileRequest.setProfile_goal("Bulking");
        profileRequest.setProfile_time("Evening");
        profileRequest.setMember_id("testuser2");
        profileRequest.setProfile_allergy("None");
        profileRequest.setProfile_diet_goal("Maintain");
        profileRequest.setProfile_sport1("Weightlifting");
        profileRequest.setProfile_sport2("Running");
        profileRequest.setProfile_sport3("Cycling");
        profileRequest.setProfile_workout_frequency(4);

        // when : 프로필 등록 메서드 호출
        int result = profileMapper.registerProfile(profileRequest);

        // then : 결과 확인 (1이 반환되면 성공)
        assertThat(result).isEqualTo(1);
    }

    @Test 
    @DisplayName("013 : ID별 유저 프로필 조회") 
    public void selectProfile() {
        // gicen : 사용자 ID
        String memberId = "testuser3";

        // 프로필 등록 (필요시)
        ProfileRequestDTO profileRequest = new ProfileRequestDTO();
        profileRequest.setProfile_id("P003");
        profileRequest.setProfile_gender("M");
        profileRequest.setProfile_age(25);
        profileRequest.setProfile_region("Seoul");
        profileRequest.setProfile_weight(70.5f);
        profileRequest.setProfile_height(175.0f);
        profileRequest.setProfile_goal("Bulking");
        profileRequest.setProfile_time("Evening");
        profileRequest.setMember_id(memberId);
        profileRequest.setProfile_allergy("None");
        profileRequest.setProfile_diet_goal("Maintain");
        profileRequest.setProfile_sport1("Weightlifting");
        profileRequest.setProfile_sport2("Running");
        profileRequest.setProfile_sport3("Cycling");
        profileRequest.setProfile_workout_frequency(4);
        
        // 프로필 등록 메서드 호출
        profileMapper.registerProfile(profileRequest);

        // when : 사용자 ID로 프로필 조회
        ProfileResponseDTO retrievedProfile = profileMapper.getUserById(memberId);

        // then : 결과 확인
        assertThat(retrievedProfile).isNotNull();  // 프로필이 null이 아니어야 함
        assertThat(retrievedProfile.getMember_id()).isEqualTo(memberId);  // member_id가 일치해야 함
        assertThat(retrievedProfile.getProfile_id()).isEqualTo("P003");  // profile_id가 일치해야 함
        assertThat(retrievedProfile.getProfile_gender()).isEqualTo("M");  // 성별이 일치해야 함
        assertThat(retrievedProfile.getProfile_age()).isEqualTo(25);  // 나이가 일치해야 함
        assertThat(retrievedProfile.getProfile_region()).isEqualTo("Seoul");  // 거주 지역이 일치해야 함
        assertThat(retrievedProfile.getProfile_weight()).isEqualTo(70.5f);  // 몸무게가 일치해야 함
        assertThat(retrievedProfile.getProfile_height()).isEqualTo(175.0f);  // 키가 일치해야 함
        assertThat(retrievedProfile.getProfile_goal()).isEqualTo("Bulking");  // 운동 목표가 일치해야 함
        assertThat(retrievedProfile.getProfile_time()).isEqualTo("Evening");  // 운동 시간대가 일치해야 함
        assertThat(retrievedProfile.getProfile_allergy()).isEqualTo("None");  // 알레르리가 일치해야 함
        assertThat(retrievedProfile.getProfile_diet_goal()).isEqualTo("Maintain");  // 식단 목표가 일치해야 함
        assertThat(retrievedProfile.getProfile_sport1()).isEqualTo("Weightlifting");  // 주력 운동 종목이 일치해야 함
        assertThat(retrievedProfile.getProfile_sport2()).isEqualTo("Running");  // 주력 운동 종목 2순위가 일치해야 함
        assertThat(retrievedProfile.getProfile_sport3()).isEqualTo("Cycling");  // 주력 운동 종목 3순위가 일치해야 함
        assertThat(retrievedProfile.getProfile_workout_frequency()).isEqualTo(4);  // 주간 운동 빈도가 일치해야 함
}

    @Test
    @DisplayName("014 : 사용자 별 프로필 수정")
    public void testUpdateProfile() {
        // given : 기존 프로필을 등록하기 위해 DTO 설정
        ProfileRequestDTO profileRequest = new ProfileRequestDTO();
        profileRequest.setProfile_id("P005");
        profileRequest.setProfile_gender("M");
        profileRequest.setProfile_age(25);
        profileRequest.setProfile_region("Seoul");
        profileRequest.setProfile_weight(70.5f);
        profileRequest.setProfile_height(175.0f);
        profileRequest.setProfile_goal("Bulking");
        profileRequest.setProfile_time("Evening");
        profileRequest.setMember_id("testuser5");
        profileRequest.setProfile_allergy("None");
        profileRequest.setProfile_diet_goal("Maintain");
        profileRequest.setProfile_sport1("Weightlifting");
        profileRequest.setProfile_sport2("Running");
        profileRequest.setProfile_sport3("Cycling");
        profileRequest.setProfile_workout_frequency(4);

        // 프로필 등록 메서드 호출
        profileMapper.registerProfile(profileRequest);

        // when:  프로필 수정할 DTO 설정
        profileRequest.setProfile_goal("Cutting"); // 목표 변경
        profileRequest.setProfile_weight(68.0f);    // 체중 변경

        // 프로필 수정 메서드 호출
        int result = profileMapper.updateProfile(profileRequest);

        // then : 결과 확인 (1이 반환되면 성공)
        assertThat(result).isEqualTo(1);
        
        // 수정된 프로필 확인
        ProfileResponseDTO updatedProfile = profileMapper.getUserById("testuser5");
        assertThat(updatedProfile.getProfile_goal()).isEqualTo("Cutting");
        assertThat(updatedProfile.getProfile_weight()).isEqualTo(68.0f);
    }

    @Test
    @DisplayName("016 : 회원 탈퇴 시 유저 프로필도 삭제됨")
    public void testDeleteUserAndProfile() {
        // 1. 회원가입 및 프로필 등록
        String memberId = "testuser9";
        
        // 사용자 등록
        UserRequestDTO initialRequest = new UserRequestDTO();
        initialRequest.setMember_id(memberId);
        initialRequest.setMember_nickname("OldNick9");
        initialRequest.setMember_password("oldpassword");
        initialRequest.setMember_email("old@example.com");
        initialRequest.setMember_phone("010-1234-5678");
        initialRequest.setMember_img("default.png");
        initialRequest.setMember_type("local");
        // 추가 필드 설정 (필요한 경우)
        
        userMapper.registerUser(initialRequest);
        
        // 프로필 등록하기 위한 객체
        ProfileRequestDTO profileRequest = new ProfileRequestDTO();
        profileRequest.setProfile_id("P006");
        profileRequest.setProfile_gender("M");
        profileRequest.setProfile_age(25);
        profileRequest.setProfile_region("Seoul");
        profileRequest.setProfile_weight(70.5f);
        profileRequest.setProfile_height(175.0f);
        profileRequest.setProfile_goal("Bulking");
        profileRequest.setProfile_time("Evening");
        profileRequest.setMember_id(memberId);
        profileRequest.setProfile_allergy("None");
        profileRequest.setProfile_diet_goal("Maintain");
        profileRequest.setProfile_sport1("Weightlifting");
        profileRequest.setProfile_sport2("Running");
        profileRequest.setProfile_sport3("Cycling");
        profileRequest.setProfile_workout_frequency(4);

        //프로필 등록 메서드 호출 
        profileMapper.registerProfile(profileRequest);
        
        // 2. 회원 탈퇴
        userMapper.deleteUser(memberId); // 회원 탈퇴 메서드 호출
        
       // 3. 프로필 삭제
    profileMapper.deleteProfileByMemberId(memberId); // 프로필 삭제 메서드 호출
    
    // 4. 프로필 존재 여부 확인
    ProfileResponseDTO deletedProfile = profileMapper.getUserById(memberId);
    assertThat(deletedProfile).isNull(); // 프로필이 null이어야 함
    }
}

