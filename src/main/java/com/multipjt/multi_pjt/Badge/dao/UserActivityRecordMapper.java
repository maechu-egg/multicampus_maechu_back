package com.multipjt.multi_pjt.badge.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.multipjt.multi_pjt.badge.domain.record.UserActivityRecordRequestDTO;
import com.multipjt.multi_pjt.badge.domain.record.UserActivityRecordResponseDTO;

@Mapper
public interface UserActivityRecordMapper {
    
    void insertActivity(UserActivityRecordRequestDTO record);
    
    // 활동 기록 추가 및 점수 업데이트
    void insertActivityAndUpdatePoints(Map<String, Object> params);

    // 같은 날 같은 활동이 있는지 확인
    @Select("SELECT COUNT(*) FROM UserActivityRecord WHERE member_id = #{memberId} AND activity_type = #{activityType} AND DATE(created_date) = CURDATE()")
    int countTodayActivity(@Param("memberId") int memberId, @Param("activityType") String activityType);

    // 특정 회원의 활동 기록을 조회하는 메서드
    List<UserActivityRecordResponseDTO> getActivitiesByMemberId(int memberId);

    // 특정 회원의 포스트에서 활동 기록을 조회하는 메서드
    List<UserActivityRecordResponseDTO> getActivitiesFromPosts(int memberId);

    // 특정 회원의 댓글에서 활동 기록을 조회하는 메서드
    List<UserActivityRecordResponseDTO> getActivitiesFromComments(int memberId);

    // 특정 회원의 운동 기록을 조회하는 메서드
    List<UserActivityRecordResponseDTO> getActivitiesFromExercises(int memberId);

    // 특정 회원의 식단 기록을 조회하는 메서드
    List<UserActivityRecordResponseDTO> getActivitiesFromDiets(int memberId);

    // 특정 회원의 총 점수를 조회하는 메서드
    List<BigDecimal> getTotalPointsByMemberId(int memberId);

}

