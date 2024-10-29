package com.multipjt.multi_pjt.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.multipjt.multi_pjt.user.dao.ProfileMapper;
import com.multipjt.multi_pjt.user.dao.UserMapper;
import com.multipjt.multi_pjt.user.domain.mypage.ProfileRequestDTO;
import com.multipjt.multi_pjt.user.domain.mypage.ProfileResponseDTO;

@SpringBootTest
@Transactional 
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
        profileRequest.setProfile_gender("M");
        profileRequest.setProfile_age(25);
        profileRequest.setProfile_region("Seoul");
        profileRequest.setProfile_weight(70.5f);
        profileRequest.setProfile_height(175.0f);
        profileRequest.setProfile_goal("Bulking");
        profileRequest.setMember_id(16);
        profileRequest.setProfile_allergy("None");
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
        int memberId = 1;

        // 프로필 등록 (필요시)
        ProfileRequestDTO profileRequest = new ProfileRequestDTO();
        profileRequest.setProfile_gender("M");
        profileRequest.setProfile_age(25);
        profileRequest.setProfile_region("Seoul");
        profileRequest.setProfile_weight(70.5f);
        profileRequest.setProfile_height(175.0f);
        profileRequest.setProfile_goal("Bulking");
        profileRequest.setMember_id(memberId);
        profileRequest.setProfile_allergy("None");
        profileRequest.setProfile_sport1("Weightlifting");
        profileRequest.setProfile_sport2("Running");
        profileRequest.setProfile_sport3("Cycling");
        profileRequest.setProfile_workout_frequency(4);
        
        // 프로필 등록 메서드 호출
    profileMapper.registerProfile(profileRequest);

    // when : 프로필 조회 메서드 호출
    ProfileResponseDTO profile = profileMapper.getUserById(memberId);

    // then : 조회된 프로필의 데이터 검증
    assertThat(profile).isNotNull();
    assertThat(profile.getProfile_gender()).isEqualTo("M");
    assertThat(profile.getProfile_age()).isEqualTo(25);
    assertThat(profile.getProfile_region()).isEqualTo("Seoul");
    assertThat(profile.getProfile_weight()).isEqualTo(70.5f);
    assertThat(profile.getProfile_height()).isEqualTo(175.0f);
    assertThat(profile.getProfile_goal()).isEqualTo("Bulking");
    assertThat(profile.getMember_id()).isEqualTo(memberId);
    assertThat(profile.getProfile_allergy()).isEqualTo("None");
    assertThat(profile.getProfile_sport1()).isEqualTo("Weightlifting");
    assertThat(profile.getProfile_sport2()).isEqualTo("Running");
    assertThat(profile.getProfile_sport3()).isEqualTo("Cycling");
    assertThat(profile.getProfile_workout_frequency()).isEqualTo(4);

       
        
}

    @Test
    @DisplayName("014 : 사용자 별 프로필 수정")
    public void testUpdateProfile() {
 // given : 사용자 ID와 새로운 프로필 정보
        int memberId = 1;

        // 새로운 프로필 생성
        ProfileRequestDTO newProfile = new ProfileRequestDTO();
        newProfile.setProfile_gender("M");
        newProfile.setProfile_age(25);
        newProfile.setProfile_region("Seoul");
        newProfile.setProfile_weight(70.5f);
        newProfile.setProfile_height(175.0f);
        newProfile.setProfile_goal("Bulking");
        newProfile.setMember_id(memberId);
        newProfile.setProfile_allergy("None");
        newProfile.setProfile_sport1("Weightlifting");
        newProfile.setProfile_sport2("Running");
        newProfile.setProfile_sport3("Cycling");
        newProfile.setProfile_workout_frequency(4);

        // when : 새로운 프로필을 DB에 등록
        int insertResult = profileMapper.registerProfile(newProfile);
        assertEquals(1, insertResult);  // 성공적으로 삽입되었는지 검증

        // 프로필 수정
        ProfileRequestDTO updatedProfile = new ProfileRequestDTO();
        updatedProfile.setProfile_gender("F");
        updatedProfile.setProfile_age(30);
        updatedProfile.setProfile_region("Busan");
        updatedProfile.setProfile_weight(65.0f);
        updatedProfile.setProfile_height(170.0f);
        updatedProfile.setProfile_goal("Cutting");
        updatedProfile.setMember_id(memberId);
        updatedProfile.setProfile_allergy("Peanuts");
        updatedProfile.setProfile_sport1("Yoga");
        updatedProfile.setProfile_sport2("Swimming");
        updatedProfile.setProfile_sport3("Cycling");
        updatedProfile.setProfile_workout_frequency(5);

        // 프로필 수정
        int updateResult = profileMapper.updateProfile(updatedProfile);
        assertEquals(1, updateResult);  // 성공적으로 수정되었는지 검증

        // 수정된 프로필 확인
        ProfileResponseDTO verifiedProfile = profileMapper.getUserById(memberId);

        // 값 검증
        assertEquals("F", verifiedProfile.getProfile_gender());
        assertEquals(30, verifiedProfile.getProfile_age());
        assertEquals("Busan", verifiedProfile.getProfile_region());
        assertEquals(65.0f, verifiedProfile.getProfile_weight());
        assertEquals(170.0f, verifiedProfile.getProfile_height());
        assertEquals("Cutting", verifiedProfile.getProfile_goal());
        assertEquals("Peanuts", verifiedProfile.getProfile_allergy());
        assertEquals("Yoga", verifiedProfile.getProfile_sport1());
        assertEquals("Swimming", verifiedProfile.getProfile_sport2());
        assertEquals("Cycling", verifiedProfile.getProfile_sport3());
        assertEquals(5, verifiedProfile.getProfile_workout_frequency());
    }
    

    @Test
    @DisplayName("016 : 회원 탈퇴 시 유저 프로필도 삭제됨")
    public void testDeleteUserAndProfile() {
       // given : 사용자 ID와 프로필 정보
        int memberId = 16;

        // 먼저, 테스트를 위해 프로필을 생성합니다.
        ProfileRequestDTO profileRequest = new ProfileRequestDTO();
        profileRequest.setProfile_gender("M");
        profileRequest.setProfile_age(25);
        profileRequest.setProfile_region("Seoul");
        profileRequest.setProfile_weight(70.5f);
        profileRequest.setProfile_height(175.0f);
        profileRequest.setProfile_goal("Bulking");
        profileRequest.setMember_id(memberId);
        profileRequest.setProfile_allergy("None");
        profileRequest.setProfile_sport1("Weightlifting");
        profileRequest.setProfile_sport2("Running");
        profileRequest.setProfile_sport3("Cycling");
        profileRequest.setProfile_workout_frequency(4);

        // 프로필 등록
        profileMapper.registerProfile(profileRequest);

        // when : 회원 탈퇴
        int withdrawalResult = userMapper.deleteUserById(memberId); // 회원 삭제 메서드 호출
        assertEquals(1, withdrawalResult);  // 성공적으로 삭제되었는지 검증

  

        // then : 프로필이 삭제되었는지 확인
        ProfileResponseDTO deletedProfile = profileMapper.getUserById(memberId);
        assertNull(deletedProfile);  // 프로필이 null이어야 함
    }
    }


