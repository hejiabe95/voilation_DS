package com.zc.intf.mapper;

import com.zc.intf.entity.StationWeatherForecast;

import java.util.List;

public interface StationWeatherMapper {

    int insert(StationWeatherForecast record);

    StationWeatherForecast selectByPrimaryKey(String id);

    List<StationWeatherForecast> selectRepeat(StationWeatherForecast record);
}