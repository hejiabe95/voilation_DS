package com.zc.intf.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class WeatherWarn implements Serializable {
    private String id;

    private String warnId;    //预警id

    private String content;         //内容

    private String defenseAdvice;   //提示

    private String publishDate;    //发布时间

    private String publishPerson;  //发布预警发布机构

    private String sign;          //预警状态，固定为：发布\解除

    private String timeOut;      //预警有效时间（小时）

    private String yujingDate;  //预警时间

    private String yujingLevel; //预警等级

    private String yujingType;    //预警类型

    private String zhms;     //描述

    private String zhsx;    //到来时间

    private String readTime;  //读取日期

    private String readHour;  //读取小时

    private Date createTime;     //创建时间

    private static final long serialVersionUID = 1L;

    public String getReadTime() {
        return readTime;
    }

    public void setReadTime(String readTime) {
        this.readTime = readTime;
    }

    public String getReadHour() {
        return readHour;
    }

    public void setReadHour(String readHour) {
        this.readHour = readHour;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDefenseAdvice() {
        return defenseAdvice;
    }

    public void setDefenseAdvice(String defenseAdvice) {
        this.defenseAdvice = defenseAdvice;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getPublishPerson() {
        return publishPerson;
    }

    public void setPublishPerson(String publishPerson) {
        this.publishPerson = publishPerson;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(String timeOut) {
        this.timeOut = timeOut;
    }

    public String getYujingDate() {
        return yujingDate;
    }

    public void setYujingDate(String yujingDate) {
        this.yujingDate = yujingDate;
    }

    public String getYujingLevel() {
        return yujingLevel;
    }

    public void setYujingLevel(String yujingLevel) {
        this.yujingLevel = yujingLevel;
    }

    public String getYujingType() {
        return yujingType;
    }

    public void setYujingType(String yujingType) {
        this.yujingType = yujingType;
    }

    public String getZhms() {
        return zhms;
    }

    public void setZhms(String zhms) {
        this.zhms = zhms;
    }

    public String getZhsx() {
        return zhsx;
    }

    public void setZhsx(String zhsx) {
        this.zhsx = zhsx;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getWarnId() {
        return warnId;
    }

    public void setWarnId(String warnId) {
        this.warnId = warnId;
    }
}