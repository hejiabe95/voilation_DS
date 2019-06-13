package com.zc.intf.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.zc.intf.entity.BasicTrainTeam;
import com.zc.intf.mapper.BasicTrainTeamMapper;

@Component
public class BasicTrainTeamService {
	
	@Autowired
	BasicTrainTeamMapper basicTrainTeamMapper;
	
	@Transactional
	public void batchInsert(List<BasicTrainTeam> basicTrainTeamList){
		basicTrainTeamMapper.batchInsert(basicTrainTeamList);
	}
	
	@Transactional
	public void deleteALL(){
		basicTrainTeamMapper.deleteALL();
	}
}
