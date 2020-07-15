package com.zc.intf.mapper;

import com.zc.intf.entity.StationLive;

public interface StationLiveMapper {

    int insert(StationLive record);

    StationLive selectByPrimaryKey(String id);

    String selectMaxTime();
}