package com.zc.intf.mapper;

import com.zc.intf.entity.TrainViolationFile;

public interface TrainViolationFileMapper {
    int deleteByPrimaryKey(String fileid);

    int insert(TrainViolationFile record);

    int insertSelective(TrainViolationFile record);

    TrainViolationFile selectByPrimaryKey(String fileid);

    int updateByPrimaryKeySelective(TrainViolationFile record);

    int updateByPrimaryKey(TrainViolationFile record);

    String queryNewTime();
}