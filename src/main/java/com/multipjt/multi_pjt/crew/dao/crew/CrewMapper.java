package com.multipjt.multi_pjt.crew.dao.crew;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;

import com.multipjt.multi_pjt.crew.domain.crew.CrewRequestDTO;
import com.multipjt.multi_pjt.crew.domain.crew.CrewResponseDTO;

@Mapper
public interface CrewMapper {
    // 크루 생성
    public void createCrewRow(CrewRequestDTO param);

    // crew_id로 크루 조회
    public List<CrewResponseDTO> selectCrewRow();
}
