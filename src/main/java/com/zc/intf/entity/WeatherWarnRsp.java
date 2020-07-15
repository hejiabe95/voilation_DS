package com.zc.intf.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class WeatherWarnRsp implements Serializable {
    private String datetime;

    private Map<String, List<WeatherWarn>> data;         //内容

    private String thour;   //提示

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public Map<String, List<WeatherWarn>> getData() {
        return data;
    }

    public void setData(Map<String, List<WeatherWarn>> data) {
        this.data = data;
    }

    public String getThour() {
        return thour;
    }

    public void setThour(String thour) {
        this.thour = thour;
    }
}