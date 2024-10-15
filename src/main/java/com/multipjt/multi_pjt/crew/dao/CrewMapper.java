package com.multipjt.multi_pjt.crew.dao;

import org.apache.ibatis.annotations.Mapper;

import com.multipjt.multi_pjt.crew.domain.crew.CrewRequestDTO;
import com.multipjt.multi_pjt.crew.domain.battle.CrewBattleRequestDTO;
@Mapper
public interface CrewMapper {
    // 크루 생성
    public void createCrew(CrewRequestDTO param);



    public void saveCrewBattleRow(CrewBattleRequestDTO params);
}
