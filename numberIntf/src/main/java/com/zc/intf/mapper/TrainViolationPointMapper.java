package com.zc.intf.mapper;

import com.zc.intf.entity.TrainViolationPoint;

public interface TrainViolationPointMapper {
    int deleteByPrimaryKey(String id);

    int insert(TrainViolationPoint record);

    int insertSelective(TrainViolationPoint record);

    TrainViolationPoint selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(TrainViolationPoint record);

    int updateByPrimaryKey(TrainViolationPoint record);

    String queryNewTime();
}