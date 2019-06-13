package com.zc.intf.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.zc.intf.entity.BasicTrainMode;
import com.zc.intf.mapper.BasicTrainModeMapper;

@Component
public class BasicTrainModeService {
	
	@Autowired
	BasicTrainModeMapper basicTrainModeMapper;
	
	@Transactional
	public void batchInsert(List<BasicTrainMode> basicTrainModeList){
		basicTrainModeMapper.batchInsert(basicTrainModeList);
	}

	
	@Transactional
	public void deleteALL(){
		basicTrainModeMapper.deleteALL();
	}
 
}
