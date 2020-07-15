package com.zc.intf.entity;

import java.io.Serializable;
import java.util.Date;

public class StationWeatherForecast implements Serializable {
    private String id;

    private String readTime;  //读取日期

    private String station_code;   //站点编码

    private String future_hour;    //未来小时

    private String tem_max;  //最高温度

    private String validHour;          //有效时间

    private String tem_min;      //最低温度

    private String weather_code;  //天气现象编码

    private String wind_d_code; //风向编码

    private String wind_s_code;    //风速编码

    private Date createTime;     //创建时间


    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReadTime() {
        return readTime;
    }

    public void setReadTime(String readTime) {
        this.readTime = readTime;
    }

    public String getStation_code() {
        return station_code;
    }

    public void setStation_code(String station_code) {
        this.station_code = station_code;
    }

    public String getFuture_hour() {
        return future_hour;
    }

    public void setFuture_hour(String future_hour) {
        this.future_hour = future_hour;
    }

    public String getTem_max() {
        return tem_max;
    }

    public void setTem_max(String tem_max) {
        this.tem_max = tem_max;
    }

    public String getValidHour() {
        return validHour;
    }

    public void setValidHour(String validHour) {
        this.validHour = validHour;
    }

    public String getTem_min() {
        return tem_min;
    }

    public void setTem_min(String tem_min) {
        this.tem_min = tem_min;
    }

    public String getWeather_code() {
        return weather_code;
    }

    public void setWeather_code(String weather_code) {
        this.weather_code = weather_code;
    }

    public String getWind_d_code() {
        return wind_d_code;
    }

    public void setWind_d_code(String wind_d_code) {
        this.wind_d_code = wind_d_code;
    }

    public String getWind_s_code() {
        return wind_s_code;
    }

    public void setWind_s_code(String wind_s_code) {
        this.wind_s_code = wind_s_code;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}