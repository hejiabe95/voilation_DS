package com.zc.intf.mapper;

import com.zc.intf.entity.WeatherWarn;

import java.util.List;

public interface WeatherWarnMapper {
    int deleteByPrimaryKey(String id);

    int insert(WeatherWarn record);

    int insertSelective(WeatherWarn record);

    WeatherWarn selectByPrimaryKey(String id);

    List<WeatherWarn> selectRepeat(WeatherWarn record);

}