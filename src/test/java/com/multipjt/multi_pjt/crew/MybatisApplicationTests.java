package com.multipjt.multi_pjt.crew;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.multipjt.multi_pjt.crew.dao.crew.CrewMapper;
import com.multipjt.multi_pjt.crew.domain.crew.CrewMemberRequestDTO;
import com.multipjt.multi_pjt.crew.domain.crew.CrewPostRequestDTO;
import com.multipjt.multi_pjt.crew.domain.crew.CrewRequestDTO;
import com.multipjt.multi_pjt.crew.domain.crew.CrewResponseDTO;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
// @Transactional
public class MybatisApplicationTests {
    
    @Autowired
    private CrewMapper crewMapper;

    // --------- 크루 찾기 ---------
    @Test
    @DisplayName("001 : 크루 생성 테스트")
    public void createCrewTest(){
        //크루 생성에 필요한 크루 정보
        CrewRequestDTO request = CrewRequestDTO.builder()
                                    .member_id(1)
                                    .crew_name("testCrew3")
                                    .crew_goal("벌크업")
                                    .crew_title("벌크업 목표 크루원 모집")
                                    .crew_location("전주")
                                    .crew_sport("헬스")
                                    .crew_gender("남성")
                                    .crew_frequency("주 6회")
                                    .crew_age("20대")
                                    .crew_date("2024-10-15 17:25:10")
                                    .build();
        //크루 생성
        crewMapper.createCrewRow(request);
        System.out.println("debug >>> 크루 생성 테스트 성공");
    }

    @Test
    @DisplayName("002 : 전체 크루 리스트 조회 테스트")
    public void selectCrewTest(){
        List<CrewResponseDTO> crewList = crewMapper.selectCrewRow();
        for (CrewResponseDTO dto:crewList){
            System.out.println(dto);
        }
        assertFalse(crewList.isEmpty(), "크루가 존재해야 합니다.");
        System.out.println("크루 전체 조회 테스트 성공");
    }

    @Test
    @DisplayName("003 : 특정 크루 리스트 조회 (종목)")
    public void selectCrewSportTest(){
        Map<String, String> map = new HashMap<>();
        map.put("crew_sport", "헬스");

        List<CrewResponseDTO> crewList = crewMapper.selectCrewSportRow(map);
        for (CrewResponseDTO dto : crewList){
            System.out.println(dto);
        }
        assertFalse(crewList.isEmpty(), "크루가 존재해야 합니다.");
        System.out.println("크루 종목 조회 테스트 성공");
    }

    @Test
    @DisplayName("004 : 특정 크루 정보 조회")
    public void selectCrewInfoTest(){
        Map<String, Integer> map = new HashMap<>();
        map.put("crew_id", 2);
        CrewResponseDTO crewInfo = crewMapper.selectCrewInfoRow(map);
        System.out.println(crewInfo);
        assertNotNull(crewInfo, "크루가 존재해야 합니다.");
        System.out.println("크루 정보 조회 테스트 성공");
    }

    @Test
    @DisplayName("005 : 크루원 추가")
    public void insertNewMemberTest(){
        CrewMemberRequestDTO Member1 = CrewMemberRequestDTO.builder()
                                    .crew_id(1)
                                    .member_id(16)
                                    .crew_member_state(0)
                                    .battle_wins(0)
                                    .build();
        crewMapper.insertCrewMemberRow(Member1);
        System.out.println("크루원 신청 테스트 성공");
    }

    // --------- 크루 소개 ---------
    @Test
    @DisplayName("006 : 크루 소개 수정")
    public void updateCrewIntroTest(){
        CrewRequestDTO intro = CrewRequestDTO.builder()
                                .crew_id(1)
                                .crew_name("up-crew1")
                                .crew_intro_img("img1")
                                .crew_intro_post("post1")
                                .build();
        crewMapper.updateCrewIntroRow(intro);
        System.out.println("크루 소개 수정 완료");
    }

    @Test
    @DisplayName("007 : 크루 관리 수정")
    public void updateCrewInfoTest(){
        CrewRequestDTO info = CrewRequestDTO.builder()
                                .crew_id(1)
                                .crew_name("up-crew2")
                                .crew_goal("크루 목표")
                                .crew_title("크루 제목")
                                .crew_location("활동 지역")
                                .crew_sport("운동 종목")
                                .crew_gender("혼성")
                                .crew_frequency("주 3회")
                                .crew_age("20대")
                                .build();
        crewMapper.updateCrewInfoRow(info);
        System.out.println("크루 관리 수정 완료");
    }

    // --------- 크루원 정보 ---------

    // --------- 크루 ---------
    // @Test
    // @DisplayName("008 : 크루 게시판 게시물 등록")
    // public void insertCrewPostTest(){
    //     CrewPostRequestDTO post = CrewPostRequestDTO.builder()
    //                                 .crew_post_title("게시글 제목")
    //                                 .crew_post_contennt("게시글 내용")
    //                                 .crew_post_img("게시글 img")
    //                                 .crew_post_like(2)
    //                                 .crew_post_state(2)
    //                                 .
    //                                 .build();
    // }
}
