package com.zc.intf.entity;

import java.io.Serializable;
import java.util.Date;

public class StationLive implements Serializable {
    private String id;

    private String datetime;         //实况观测时间

    private String pre_p1h;   //小时内的累计降水（即整点0分到现在时间的的降水累积值），单位：mm

    private String wind_d;    //风向，单位：角度

    private String wind_s;  //风速，单位：m/s

    private String roadtem;          //轨温

    private String stationid;      //站号信息

    private Date createTime;     //创建时间

    private String readTime;  //读取日期

    private static final long serialVersionUID = 1L;

    public String getReadTime() {
        return readTime;
    }

    public void setReadTime(String readTime) {
        this.readTime = readTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getPre_p1h() {
        return pre_p1h;
    }

    public void setPre_p1h(String pre_p1h) {
        this.pre_p1h = pre_p1h;
    }

    public String getWind_d() {
        return wind_d;
    }

    public void setWind_d(String wind_d) {
        this.wind_d = wind_d;
    }

    public String getWind_s() {
        return wind_s;
    }

    public void setWind_s(String wind_s) {
        this.wind_s = wind_s;
    }

    public String getRoadtem() {
        return roadtem;
    }

    public void setRoadtem(String roadtem) {
        this.roadtem = roadtem;
    }

    public String getStationid() {
        return stationid;
    }

    public void setStationid(String stationid) {
        this.stationid = stationid;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}