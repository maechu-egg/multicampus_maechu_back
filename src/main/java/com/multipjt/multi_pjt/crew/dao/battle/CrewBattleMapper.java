package com.multipjt.multi_pjt.crew.dao.battle;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.multipjt.multi_pjt.crew.domain.battle.BattleMemberRequestDTO;
import com.multipjt.multi_pjt.crew.domain.battle.BattleMemberResponseDTO;
import com.multipjt.multi_pjt.crew.domain.battle.CrewBattleFeedRequestDTO;
import com.multipjt.multi_pjt.crew.domain.battle.CrewBattleFeedResponseDTO;
import com.multipjt.multi_pjt.crew.domain.battle.CrewBattleRequestDTO;
import com.multipjt.multi_pjt.crew.domain.battle.CrewBattleResponseDTO;
import com.multipjt.multi_pjt.crew.domain.battle.CrewVoteRequestDTO;

@Mapper
public interface CrewBattleMapper {

    public void saveCrewBattleRow(CrewBattleRequestDTO params);
    
    public List<CrewBattleResponseDTO> selectCrewBattleRow();

    public void saveBattleMemberRow(BattleMemberRequestDTO params);

    public List<BattleMemberResponseDTO> selectBattleMemberRow();

    public void saveCrewBattleFeedRow(CrewBattleFeedRequestDTO params);

    public List<CrewBattleFeedResponseDTO> selectCrewBattleFeedRow();

    public void saveVoteRow(CrewVoteRequestDTO params);
} 
