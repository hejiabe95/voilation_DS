package com.zc.intf.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class TrainViolationPoint implements Serializable {
    private String id;

    private BigDecimal code;

    private String note;

    private String starttime;

    private String endtime;

    private BigDecimal glb;

    private BigDecimal speed;

    private BigDecimal tubepressure;

    private BigDecimal traction;

    private String workingcondition;

    private BigDecimal limitspeed;

    private String signalstate;

    private String signaltype;

    private BigDecimal totalpressure;

    private BigDecimal averagepressure;

    private BigDecimal brakepressure;

    private String advice;

    private String fileid;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public BigDecimal getCode() {
        return code;
    }

    public void setCode(BigDecimal code) {
        this.code = code;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note == null ? null : note.trim();
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime == null ? null : starttime.trim();
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime == null ? null : endtime.trim();
    }

    public BigDecimal getGlb() {
        return glb;
    }

    public void setGlb(BigDecimal glb) {
        this.glb = glb;
    }

    public BigDecimal getSpeed() {
        return speed;
    }

    public void setSpeed(BigDecimal speed) {
        this.speed = speed;
    }

    public BigDecimal getTubepressure() {
        return tubepressure;
    }

    public void setTubepressure(BigDecimal tubepressure) {
        this.tubepressure = tubepressure;
    }

    public BigDecimal getTraction() {
        return traction;
    }

    public void setTraction(BigDecimal traction) {
        this.traction = traction;
    }

    public String getWorkingcondition() {
        return workingcondition;
    }

    public void setWorkingcondition(String workingcondition) {
        this.workingcondition = workingcondition == null ? null : workingcondition.trim();
    }

    public BigDecimal getLimitspeed() {
        return limitspeed;
    }

    public void setLimitspeed(BigDecimal limitspeed) {
        this.limitspeed = limitspeed;
    }

    public String getSignalstate() {
        return signalstate;
    }

    public void setSignalstate(String signalstate) {
        this.signalstate = signalstate == null ? null : signalstate.trim();
    }

    public String getSignaltype() {
        return signaltype;
    }

    public void setSignaltype(String signaltype) {
        this.signaltype = signaltype == null ? null : signaltype.trim();
    }

    public BigDecimal getTotalpressure() {
        return totalpressure;
    }

    public void setTotalpressure(BigDecimal totalpressure) {
        this.totalpressure = totalpressure;
    }

    public BigDecimal getAveragepressure() {
        return averagepressure;
    }

    public void setAveragepressure(BigDecimal averagepressure) {
        this.averagepressure = averagepressure;
    }

    public BigDecimal getBrakepressure() {
        return brakepressure;
    }

    public void setBrakepressure(BigDecimal brakepressure) {
        this.brakepressure = brakepressure;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice == null ? null : advice.trim();
    }

    public String getFileid() {
        return fileid;
    }

    public void setFileid(String fileid) {
        this.fileid = fileid == null ? null : fileid.trim();
    }
}