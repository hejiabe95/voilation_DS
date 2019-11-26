package com.zc.intf.util;

import com.zc.intf.entity.BasicTrainMode;
import com.zc.intf.entity.BasicTrainTeam;
import com.zc.intf.entity.TrainNumberViolation;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DSUtil {

    /**
     * ------------------业务
     */

    public static String credential = "userName=zhusuoAPI&password=abcd@1234..";

    /**
     * 1.机车得分表URL
     * @param startTime
     * @param endTime
     * @return
     */
    public static String getUrlTrainNumberViolaion(String startTime, String endTime, int start){

        return "http://27.128.164.147:9600/WebExternal/getFileBeanByParam.do?"
                +"teamID&modeID&startTime="
                +startTime
                +"&endTime="
                +endTime
                +"&trainNum&trainID"
                +"&start="
                +start
                +"&length=100&"
                +credential;

    }

    /**
     * 1.机车得分表json转换
     * @param object
     * @return
     */
    public static TrainNumberViolation jsonToTrainNumberViolation(JSONObject object){
        TrainNumberViolation trainNumberViolation = new TrainNumberViolation();
        String fileId = optString(object,"fileId");
        trainNumberViolation.setFileId(fileId);
        String filePath = optString(object,"filePath");
        trainNumberViolation.setFilePath(filePath);
        int number = object.getInt("number");
        trainNumberViolation.setNumber(number);
        String departureTime = optString(object,"departureTime");
        trainNumberViolation.setDepartureTime(departureTime);
        String departureTimems = optString(object,"departureTimems");
        trainNumberViolation.setDepartureTimems(departureTimems);
        int trainNum = object.getInt("trainNum");
        trainNumberViolation.setTrainNum(trainNum);
        String trainId = optString(object,"trainId");
        trainNumberViolation.setTrainId(trainId);
        String driverName = optString(object,"driverName");
        trainNumberViolation.setDriverName(driverName);
        String viceDriverName = optString(object,"viceDriverName");
        trainNumberViolation.setViceDriverName(viceDriverName);
        int roadId = object.getInt("roadId");
        trainNumberViolation.setRoadId(roadId);
        int total = object.getInt("total");
        trainNumberViolation.setTotal(total);
        int length = object.getInt("length");
        trainNumberViolation.setLength(length);
        String departureStation = optString(object,"departureStation");
        trainNumberViolation.setDepartureStation(departureStation);
        String passengerFreight = optString(object,"passengerFreight");
        trainNumberViolation.setPassengerFreight(passengerFreight);
        String trainType =  optString(object,"trainType");
        trainNumberViolation.setTrainType(trainType);
        int violationNum = object.getInt("violationNum");
        trainNumberViolation.setViolationNum(violationNum);
        int deductMarks = object.getInt("deductMarks");
        trainNumberViolation.setDeductMarks(deductMarks);
        int score = object.getInt("score");
        trainNumberViolation.setScore(score);
        String thisIt = optString(object,"thisIt");
        trainNumberViolation.setThisIt(thisIt);
        String violationRemark = object.getString("violationRemark");
        trainNumberViolation.setViolationRemark(violationRemark);
        String modeID = optString(object,"modeID");
        trainNumberViolation.setModeID(modeID);
        String modeName = optString(object,"modeName");
        trainNumberViolation.setModeName(modeName);
        String teamID = optString(object,"teamID");
        trainNumberViolation.setTeamID(teamID);
        String teamName = optString(object,"teamName");
        trainNumberViolation.setTeamName(teamName);
        return trainNumberViolation;
    }


    /**
     * 2.机车班组码表URL
     * @return
     */
    public static String getUrlBasicTrainTeam(){
        return "http://27.128.164.147:9600/WebExternal/getAllTeam.do?"+credential;
    }

    /**
     * 2.机车班组码表JSON转换
     * @param object
     * @return
     */
    public static BasicTrainTeam jsonToBasicTrainTeam(JSONObject object){

        BasicTrainTeam basicTrainTeam = new BasicTrainTeam();
        int number = object.getInt("number");
        basicTrainTeam.setNumber(number);
        int id = object.getInt("id");
        basicTrainTeam.setId(id);
        String teamName = optString(object,"teamName");
        basicTrainTeam.setTeamName(teamName);
        String teamNote = optString(object,"teamNote");
        basicTrainTeam.setTeamNote(teamNote);
        String teamParentID = optString(object,"teamParentID");
        basicTrainTeam.setTeamParentID(teamParentID);
        return basicTrainTeam;
    }

    /**
     * 3.机车模式码表URL
     * @return
     */
    public static String getUrlBasicTrainMode(){
        return "http://27.128.164.147:9600/WebExternal/getAllModeEntity.do?"+credential;
    }

    /**
     * 3.机车模式码表JSON转换
     * @param object
     * @return
     */
    public static BasicTrainMode jsonToBasicTrainMode(JSONObject object){

        BasicTrainMode basicTrainMode = new BasicTrainMode();
        int id = object.getInt("id");
        basicTrainMode.setId(id);
        String modeName = optString(object,"modeName");
        basicTrainMode.setModeName(modeName);
        int modeMinTotal = object.getInt("modeMinTotal");
        basicTrainMode.setModeMinTotal(modeMinTotal);
        int modeMaxTotal = object.getInt("modeMaxTotal");
        basicTrainMode.setModeMaxTotal(modeMaxTotal);
        String modeSsx = optString(object,"modeSsx");
        basicTrainMode.setModeSsx(modeSsx);
        String modeTrainTypes = optString(object,"modeTrainTypes");
        basicTrainMode.setModeTrainTypes(modeTrainTypes);
        int modeMinCarNum = object.getInt("modeMinCarNum");
        basicTrainMode.setModeMinCarNum(modeMinCarNum);
        int modeMaxCarNum = object.getInt("modeMaxCarNum");
        basicTrainMode.setModeMaxCarNum(modeMaxCarNum);
        int modemintrainID = object.getInt("modeMinTrainID");
        basicTrainMode.setModemintrainID(modemintrainID);
        int modemaxtrainID = object.getInt("modeMaxTrainNum");
        basicTrainMode.setModemaxtrainID(modemaxtrainID);
        int modeMinTrainNum = object.getInt("modeMinTrainNum");
        basicTrainMode.setModeMinTrainNum(modeMinTrainNum);
        int modeMaxTrainNum = object.getInt("modeMaxTrainNum");
        basicTrainMode.setModeMaxTrainNum(modeMaxTrainNum);
        return basicTrainMode;
    }


    /**
     * --------------------通用
     */


    /**
     * 防止 空的JSON字段
     * @param json
     * @param key
     * @return
     */
    public static String optString(JSONObject json, String key) {
        if (json.isNull(key)) {
            return null;
        } else {
            return json.optString(key, null);
        }
    }


    /**
     * 获取当前时间，年月日
     * @return
     */
    public static String getTime() {
        SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd");
        return sdfTime.format(new Date());
    }

    /**
     * 获取当前时间减去一个小时
     * @return
     */
    public static String getTimejianyi() {
        Calendar calen = Calendar.getInstance();//可以对每个时间域单独修改
        calen.setTime(new Date());
        calen.set(Calendar.HOUR_OF_DAY, calen.get(Calendar.HOUR_OF_DAY) - 1);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH");
        String time = format.format(calen.getTime());
        return time;
    }

}
