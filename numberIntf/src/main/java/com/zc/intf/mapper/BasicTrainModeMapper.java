package com.zc.intf.mapper;

import java.util.List;

import com.zc.intf.entity.BasicTrainMode;

public interface BasicTrainModeMapper {
 

    int batchInsert(List<BasicTrainMode> basicTrainModeList);
    
	void deleteALL();

     
}