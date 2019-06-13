package com.zc.intf.mapper;

import java.util.List;

import com.zc.intf.entity.TrainNumberViolation;

public interface TrainNumberMapper {
 

    int batchInsert(List<TrainNumberViolation> trainNumberViolationList);
    
    int quertyByFileId(String fileId);

     
}