package com.zc.intf.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class TrainViolationFile implements Serializable {
    private String fileid;

    private BigDecimal jiaoluhao;

    private String locotype;

    private BigDecimal locoteamid;

    private String locoteamname;

    private String loconumber;

    private BigDecimal trainnumber;

    private BigDecimal driverteamid;

    private String driverteamname;

    private BigDecimal driverjobnumber;

    private String drivername;

    private BigDecimal vicedriverjobnumber;

    private String vicedrivername;

    private BigDecimal totalweight;

    private BigDecimal length;

    private BigDecimal vehiclenumber;

    private String kehuo;

    private String benbu;

    private String departuretime;

    private String departurestation;

    private BigDecimal violationnumber;

    private BigDecimal score;

    private BigDecimal deductmarks;

    private String violationremark;

    private BigDecimal modeid;

    private BigDecimal analysisstate;

    private String analysistime;

    private String traintype;

    private static final long serialVersionUID = 1L;

    public String getFileid() {
        return fileid;
    }

    public void setFileid(String fileid) {
        this.fileid = fileid == null ? null : fileid.trim();
    }

    public BigDecimal getJiaoluhao() {
        return jiaoluhao;
    }

    public void setJiaoluhao(BigDecimal jiaoluhao) {
        this.jiaoluhao = jiaoluhao;
    }

    public String getLocotype() {
        return locotype;
    }

    public void setLocotype(String locotype) {
        this.locotype = locotype == null ? null : locotype.trim();
    }

    public BigDecimal getLocoteamid() {
        return locoteamid;
    }

    public void setLocoteamid(BigDecimal locoteamid) {
        this.locoteamid = locoteamid;
    }

    public String getLocoteamname() {
        return locoteamname;
    }

    public void setLocoteamname(String locoteamname) {
        this.locoteamname = locoteamname == null ? null : locoteamname.trim();
    }

    public String getLoconumber() {
        return loconumber;
    }

    public void setLoconumber(String loconumber) {
        this.loconumber = loconumber == null ? null : loconumber.trim();
    }

    public BigDecimal getTrainnumber() {
        return trainnumber;
    }

    public void setTrainnumber(BigDecimal trainnumber) {
        this.trainnumber = trainnumber;
    }

    public BigDecimal getDriverteamid() {
        return driverteamid;
    }

    public void setDriverteamid(BigDecimal driverteamid) {
        this.driverteamid = driverteamid;
    }

    public String getDriverteamname() {
        return driverteamname;
    }

    public void setDriverteamname(String driverteamname) {
        this.driverteamname = driverteamname == null ? null : driverteamname.trim();
    }

    public BigDecimal getDriverjobnumber() {
        return driverjobnumber;
    }

    public void setDriverjobnumber(BigDecimal driverjobnumber) {
        this.driverjobnumber = driverjobnumber;
    }

    public String getDrivername() {
        return drivername;
    }

    public void setDrivername(String drivername) {
        this.drivername = drivername == null ? null : drivername.trim();
    }

    public BigDecimal getVicedriverjobnumber() {
        return vicedriverjobnumber;
    }

    public void setVicedriverjobnumber(BigDecimal vicedriverjobnumber) {
        this.vicedriverjobnumber = vicedriverjobnumber;
    }

    public String getVicedrivername() {
        return vicedrivername;
    }

    public void setVicedrivername(String vicedrivername) {
        this.vicedrivername = vicedrivername == null ? null : vicedrivername.trim();
    }

    public BigDecimal getTotalweight() {
        return totalweight;
    }

    public void setTotalweight(BigDecimal totalweight) {
        this.totalweight = totalweight;
    }

    public BigDecimal getLength() {
        return length;
    }

    public void setLength(BigDecimal length) {
        this.length = length;
    }

    public BigDecimal getVehiclenumber() {
        return vehiclenumber;
    }

    public void setVehiclenumber(BigDecimal vehiclenumber) {
        this.vehiclenumber = vehiclenumber;
    }

    public String getKehuo() {
        return kehuo;
    }

    public void setKehuo(String kehuo) {
        this.kehuo = kehuo == null ? null : kehuo.trim();
    }

    public String getBenbu() {
        return benbu;
    }

    public void setBenbu(String benbu) {
        this.benbu = benbu == null ? null : benbu.trim();
    }

    public String getDeparturetime() {
        return departuretime;
    }

    public void setDeparturetime(String departuretime) {
        this.departuretime = departuretime == null ? null : departuretime.trim();
    }

    public String getDeparturestation() {
        return departurestation;
    }

    public void setDeparturestation(String departurestation) {
        this.departurestation = departurestation == null ? null : departurestation.trim();
    }

    public BigDecimal getViolationnumber() {
        return violationnumber;
    }

    public void setViolationnumber(BigDecimal violationnumber) {
        this.violationnumber = violationnumber;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public BigDecimal getDeductmarks() {
        return deductmarks;
    }

    public void setDeductmarks(BigDecimal deductmarks) {
        this.deductmarks = deductmarks;
    }

    public String getViolationremark() {
        return violationremark;
    }

    public void setViolationremark(String violationremark) {
        this.violationremark = violationremark == null ? null : violationremark.trim();
    }

    public BigDecimal getModeid() {
        return modeid;
    }

    public void setModeid(BigDecimal modeid) {
        this.modeid = modeid;
    }

    public BigDecimal getAnalysisstate() {
        return analysisstate;
    }

    public void setAnalysisstate(BigDecimal analysisstate) {
        this.analysisstate = analysisstate;
    }

    public String getAnalysistime() {
        return analysistime;
    }

    public void setAnalysistime(String analysistime) {
        this.analysistime = analysistime == null ? null : analysistime.trim();
    }

    public String getTraintype() {
        return traintype;
    }

    public void setTraintype(String traintype) {
        this.traintype = traintype;
    }
}