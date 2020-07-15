package com.zc.intf.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class StationWeatherRsp implements Serializable {
    private String datetime;

    private Map<String, Map<String,StationWeatherForecast>> data;         //内容

    private String thour;   //提示

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public Map<String, Map<String, StationWeatherForecast>> getData() {
        return data;
    }

    public void setData(Map<String, Map<String, StationWeatherForecast>> data) {
        this.data = data;
    }

    public String getThour() {
        return thour;
    }

    public void setThour(String thour) {
        this.thour = thour;
    }
}