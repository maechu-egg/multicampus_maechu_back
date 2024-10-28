// package com.multipjt.multi_pjt.badge.domain.badge;


// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.never;
// import static org.mockito.Mockito.verify;
// import static org.mockito.Mockito.when;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.ArgumentCaptor;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.Mockito;
// import org.mockito.MockitoAnnotations;

// import com.multipjt.multi_pjt.badge.dao.MemberBadgeMapper;
// import com.multipjt.multi_pjt.badge.dao.UserActivityRecordMapper;
// import com.multipjt.multi_pjt.badge.service.ActivityPointService;
// import com.multipjt.multi_pjt.badge.service.BadgeService;
// import com.multipjt.multi_pjt.badge.service.MemberBadgeManager;

// public class MemberBadgeManagerTest {

//     @Mock
//     private ActivityPointService activityPointService;

//     @Mock
//     private BadgeService badgeService;

//     @Mock
//     private MemberBadgeMapper memberBadgeMapper;

//     @Mock
//     private UserActivityRecordMapper userActivityRecordMapper;

//     @InjectMocks
//     private MemberBadgeManager memberBadgeManager;

//     @BeforeEach
//     public void setUp() {
//         MockitoAnnotations.openMocks(this);
//     }

//     // 첫 활동에 대한 포인트 적립 및 뱃지 생성
//     @Test
//     public void testProcessActivity_firstActivity_createsBadge() {
//         int memberId = 1;
//         String activityType = "POST";  
//         float points = 0.5f;
    
//         when(activityPointService.calculateActivityPoints(activityType)).thenReturn(points);
//         when(memberBadgeMapper.getselectBadgeByMemberId(memberId)).thenReturn(null);
    
//         // 첫 활동 기록 처리
//         memberBadgeManager.processActivity(memberId, activityType);
    
//         // 뱃지 생성 검증
//         ArgumentCaptor<MemberBadgeRequestDTO> badgeCaptor = ArgumentCaptor.forClass(MemberBadgeRequestDTO.class);
//         verify(memberBadgeMapper).insertBadge(badgeCaptor.capture());
//         MemberBadgeRequestDTO createdBadge = badgeCaptor.getValue();
    
//         assertEquals(points, createdBadge.getCurrent_points());
//         assertEquals("No Badge", createdBadge.getBadge_level()); // 첫 활동은 10점 미만이므로 No Badge
//         assertEquals(memberId, createdBadge.getMember_id());
//     }

//     // 하루에 한 번만 포인트가 적립되는 기능 확인 (게시글 및 댓글)
//     @Test
//     public void testProcessActivity_postAndComment_LimitedToOncePerDay() {
//         int memberId = 1;
//         String activityType = "POST";

//         // 오늘 해당 활동이 이미 존재한다고 설정
//         Mockito.when(userActivityRecordMapper.countTodayActivity(memberId, activityType)).thenReturn(1);

//         // 포인트 계산 호출 방지 (이미 활동이 있었음)
//         memberBadgeManager.processActivity(memberId, activityType);

//         // 포인트 계산 및 뱃지 생성/업데이트가 호출되지 않음
//         Mockito.verify(activityPointService, Mockito.never()).calculateActivityPoints(Mockito.anyString());
//         Mockito.verify(memberBadgeMapper, Mockito.never()).insertBadge(Mockito.any());
//         Mockito.verify(memberBadgeMapper, Mockito.never()).updateBadge(Mockito.any());
//     }

//     // 기존 뱃지가 있을 때 포인트와 뱃지 등급 업데이트
//     @Test
//     public void testProcessActivity_existingBadge_upgradesBadge() {
//     int memberId = 1;
//     String activityType = "DIET";
//     float points = 0.5f;

//     // 기존 뱃지 정보 설정
//     MemberBadgeResponseDTO existingBadge = new MemberBadgeResponseDTO();
//     existingBadge.setBadge_id(1);
//     existingBadge.setCurrent_points(9.5f); // 9.5점이 이미 있음

//     when(activityPointService.calculateActivityPoints(activityType)).thenReturn(points);
//     when(memberBadgeMapper.getselectBadgeByMemberId(memberId)).thenReturn(existingBadge);
//     when(badgeService.getBadgeLevel(10.0f)).thenReturn("Bronze");

//     // 기존 뱃지 업데이트 처리
//     memberBadgeManager.processActivity(memberId, activityType);

//     // 포인트 업데이트와 뱃지 등급 업데이트 검증
//     ArgumentCaptor<MemberBadgeRequestDTO> badgeCaptor = ArgumentCaptor.forClass(MemberBadgeRequestDTO.class);
//     verify(memberBadgeMapper).updateBadge(badgeCaptor.capture());
//     MemberBadgeRequestDTO updatedBadge = badgeCaptor.getValue();

//     assertEquals(10.0f, updatedBadge.getCurrent_points()); // 총 포인트는 10.0
//     assertEquals("Bronze", updatedBadge.getBadge_level());  // 뱃지 등급은 Bronze
//     assertEquals(memberId, updatedBadge.getMember_id());
// }

//     // 잘못된 활동 유형이 들어왔을 때 포인트가 적립되지 않는지 확인
//     @Test
//     public void testProcessActivity_invalidActivityType_noPoints() {
//         int memberId = 1;
//         String activityType = "INVALID"; // 잘못된 활동 유형

//         // 포인트 계산 시 잘못된 활동 유형에 대해 0 포인트 반환
//         Mockito.when(activityPointService.calculateActivityPoints(activityType)).thenReturn(0f);

//         // 활동 처리 메서드 실행
//         memberBadgeManager.processActivity(memberId, activityType);

//         // 포인트 계산이 호출되지 않았음을 검증
//         Mockito.verify(activityPointService, Mockito.never()).calculateActivityPoints(Mockito.anyString());

//         // 뱃지 생성 및 업데이트 메서드가 호출되지 않았는지 확인
//         Mockito.verify(memberBadgeMapper, Mockito.never()).insertBadge(Mockito.any());
//         Mockito.verify(memberBadgeMapper, Mockito.never()).updateBadge(Mockito.any());
//     }

// }
