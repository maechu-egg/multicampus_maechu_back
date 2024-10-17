package com.multipjt.multi_pjt.crew.dao.crew;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

import com.multipjt.multi_pjt.crew.domain.crew.CrewRequestDTO;
import com.multipjt.multi_pjt.crew.domain.crew.CrewResponseDTO;

@Mapper
public interface CrewMapper {
    // 크루 생성
    public void createCrewRow(CrewRequestDTO param);

    // 전체 크루 리스트 조회
    public List<CrewResponseDTO> selectCrewRow();

    // 특정 크루 리스트 조회 (종목)
    public List<CrewResponseDTO> selectCrewSportRow(String crew_sport);

    // 특정 크루 정보 조회
    public CrewResponseDTO selectCrewInfoRow(Integer crew_id);
}
