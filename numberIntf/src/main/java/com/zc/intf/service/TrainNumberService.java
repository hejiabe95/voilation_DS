package com.zc.intf.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.zc.intf.util.CoreHttpUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.zc.intf.entity.TrainNumberViolation;
import com.zc.intf.mapper.TrainNumberMapper;

import javax.annotation.Resource;

import static com.zc.intf.util.DSUtil.*;

@Component
public class TrainNumberService {

    private static final Logger log = LoggerFactory.getLogger(TrainNumberService.class);


    @Resource
    TrainNumberMapper trainNumberMapper;

    @Transactional(rollbackFor = Exception.class)
    public void batchInsert(List<TrainNumberViolation> trainNumberViolationList) {
        trainNumberMapper.batchInsert(trainNumberViolationList);
    }

    public int quertyByFileId(String fileId) {
        return trainNumberMapper.quertyByFileId(fileId);
    }

    public String queryNewTime() {
        return trainNumberMapper.queryNewTime();
    }

    /**
     * 机车得分表数据同步
     */
    public void doDS() throws ParseException {
        log.info("开始同步 pcs_train_number_violation ！！");
        String url = "";
        //获取之前同步的最后时间作为开始时间
        String lodNewTime = queryNewTime();

        //如果非第一次同步就就用老数据的最晚时间
        if (null != lodNewTime && !"".equals(lodNewTime)) {
            url = getUrlTrainNumberViolaion(lodNewTime, getNowTime(), 0);
        } else {
            //第一次同步就用当前时间减一个小时
            url = getUrlTrainNumberViolaion(getTimePlus(getNowTime(), Calendar.HOUR, -1), getNowTime(), 0);
        }


        //调用接口
        String allinfo = CoreHttpUtils.post(url, null);

        //如果没有返回就不处理了
        if (null == allinfo) {
            return;
        }

        //把返回值转换成json
        JSONObject jsonObject = new JSONObject(allinfo);

        //获取状态码
        int code = jsonObject.getInt("code");

        //获取信息
        String msg = jsonObject.getString("msg");


        //不成功记录下来
        if (1 != code) {
            System.out.println("syn data is error, msg ========" + msg);
            return;
        }

        //获取状态码
        int resultNum = jsonObject.getInt("resultNum");

        //如果大于100条需要分批次处理
        if (resultNum > 100) {
            for (int i = 0; i < resultNum; i = i + 100) {
                List<TrainNumberViolation> list = new ArrayList<TrainNumberViolation>();
                String url1 = "";
                if (null != lodNewTime && !"".equals(lodNewTime)) {
                    url1 = getUrlTrainNumberViolaion(lodNewTime, getNowTime(), i);
                } else {
                    url1 = getUrlTrainNumberViolaion(getTimePlus(getNowTime(), Calendar.HOUR, -1), getNowTime(), i);
                }

                //调用接口
                String allinfo1 = CoreHttpUtils.post(url1, null);

                //把返回值转换成json
                JSONObject jsonObject1 = new JSONObject(allinfo1);

                //循环数据
                setTrainNumberViolation(list, jsonObject1);

                //有数据就入库
                if (null != list && list.size() > 0) {
                    batchInsert(list);
                }
            }

        } else {
            List<TrainNumberViolation> list = new ArrayList<TrainNumberViolation>();

            setTrainNumberViolation(list, jsonObject);
            //有数据就入库
            if (null != list && list.size() > 0) {
                batchInsert(list);
            }
        }
        log.info(" pcs_train_number_violation 同步完成！！");
    }

    /**
     * 机车评分表json转换
     *
     * @param list
     * @param jsonObject1
     */
    private void setTrainNumberViolation(List<TrainNumberViolation> list, JSONObject jsonObject1) {

        for (Object info : jsonObject1.getJSONArray("data")) {
            JSONObject object = (JSONObject) info;
            String fileId = optString(object, "fileId");
            int count = quertyByFileId(fileId);
            //已经存在的不入库
            if (count > 0) {
                continue;
            }
            list.add(jsonToTrainNumberViolation(object));

        }
    }

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
}
