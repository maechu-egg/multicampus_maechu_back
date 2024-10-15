package com.multipjt.multi_pjt.crew.dao;

import org.apache.ibatis.annotations.Mapper;

import com.multipjt.multi_pjt.crew.domain.battle.CrewBattleRequestDTO;

@Mapper
public interface CrewMapper {

    public void saveCrewBattleRow(CrewBattleRequestDTO params);
}
