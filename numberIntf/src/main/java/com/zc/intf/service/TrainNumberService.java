package com.zc.intf.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.zc.intf.entity.TrainNumberViolation;
import com.zc.intf.mapper.TrainNumberMapper;

@Component
public class TrainNumberService {
	
	@Autowired
	TrainNumberMapper trainNumberMapper;
	
	@Transactional
	public void batchInsert(List<TrainNumberViolation> trainNumberViolationList){
		trainNumberMapper.batchInsert(trainNumberViolationList);
	}
}
