package com.zc.intf.mapper;

import java.util.List;

import com.zc.intf.entity.BasicTrainTeam;

public interface BasicTrainTeamMapper {
 

    int batchInsert(List<BasicTrainTeam> basicTrainTeamList);
    
    void deleteALL();

     
}