package com.zc.intf.service;

import com.alibaba.fastjson.JSONObject;
import com.zc.intf.entity.TrainViolationFile;
import com.zc.intf.entity.TrainViolationPoint;
import com.zc.intf.mapper.TrainViolationPointMapper;
import com.zc.intf.util.CoreHttpUtils;
import com.zc.intf.util.DSUtil;
import com.zc.intf.util.JsonUtil;
import org.apache.http.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLRecoverableException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.zc.intf.util.DSUtil.*;
import static com.zc.intf.util.DSUtil.getNowTime;

@Service
public class TrainViolationPointService {

    private static final Logger log = LoggerFactory.getLogger(TrainViolationPointService.class);

    @Resource
    TrainViolationPointMapper trainViolationPointMapper;


    @Async
    public void doDS() throws ParseException {

        log.info("开始同步 pcs_train_violation_point ！！");

        String url = "";

        String jsonRespond = "";

        String startTime = "";

        String endTime = "";

        //获取之前同步的最后时间作为开始时间
        String lodNewTime = trainViolationPointMapper.queryNewTime();

        //如果非第一次同步就就用老数据的最晚时间
        if (null != lodNewTime && !"".equals(lodNewTime)) {
            startTime = getTimePlus(lodNewTime, -lengthOfDay);
            endTime = getTimePlus(getNowTime(), lengthOfDay);
        } else {
            //第一次同步就用当前时间减一个月
            startTime = getTimePlus(getNowTime(), Calendar.MONTH, -1);
            endTime = getTimePlus(getNowTime(), lengthOfDay);
        }

        //如果前后超过一天，每次仅仅请求一天的量
        if (DSUtil.timeDiffOverOneDay(startTime, endTime)) {
            String loopStartTime = "";
            String loopEndTime = "";
            //间隔整数天
            int days = DSUtil.diffTimeDividedByDay(startTime, endTime);
            //求余
            Long remainder = DSUtil.remainderOfDividedByDay(startTime, endTime);

            //整数处理
            for (int i = 0; i < days; i++) {
                loopStartTime = getTimePlus(startTime, i * lengthOfDay);
                loopEndTime = getTimePlus(startTime, (i + 1) * lengthOfDay - 1);
                url = getUrlTrainViolationPoint(loopStartTime, loopEndTime);
                //调用接口
                jsonRespond = CoreHttpUtils.post(url, null);
                try {
                    insertJsonStringtoDatabase(jsonRespond);
                } catch (HttpException e) {
                    log.error(e.getMessage() + "  url:  " + url);
                }
            }

            //余数处理
            url = getUrlTrainViolationPoint(getTimePlus(loopEndTime, 1),
                    getTimePlus(loopEndTime, 1 + remainder));
            //调用接口
            jsonRespond = CoreHttpUtils.post(url, null);
            try {
                insertJsonStringtoDatabase(jsonRespond);
            } catch (HttpException e) {
                log.error(e.getMessage() + "  url:  " + url);
            }

        } else {

            url = getUrlTrainViolationPoint(startTime, endTime);
            //调用接口
            jsonRespond = CoreHttpUtils.post(url, null);
            try {
                insertJsonStringtoDatabase(jsonRespond);
            } catch (HttpException e) {
                log.error(e.getMessage() + "  url:  " + url);
            }
        }


        log.info(" pcs_train_violation_point 同步完成！！");


    }

    private void insertJsonStringtoDatabase(String allinfo) throws HttpException {

        List<TrainViolationPoint> dataList = new ArrayList<>();

        //如果没有返回就不处理了
        if (null == allinfo) {
            return;
        }

        //把返回值转换成json
        JSONObject jsonObject = JSONObject.parseObject(allinfo);

        //获取状态码
        int code = jsonObject.getIntValue("code");

        //获取信息
        String msg = jsonObject.getString("msg");

        //不成功记录下来
        if (1 != code) {
            throw new HttpException("数据同步请求接口失败： " + msg);
        }

        setTrainViolationPoint(dataList, jsonObject);

        dataList.forEach(trainViolationPoint -> {
            try {
                trainViolationPointMapper.insert(trainViolationPoint);
            } catch (Exception e) {
                if (e.getMessage().contains("ORA-00001")) {
                    log.info(e.getMessage());
                } else {
                    log.error(e.getMessage());
                }
            }
        });
    }

    /**
     * 机车违规项点表json转换
     *
     * @param list
     * @param jsonObject
     */
    private void setTrainViolationPoint(List<TrainViolationPoint> list, JSONObject jsonObject) {

        for (Object info : jsonObject.getJSONArray("data")) {
            JSONObject object = (JSONObject) info;
            list.add(jsonToTrainViolationPoint(object));
        }
    }


    /**
     * 4.机车违规项点表URL
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static String getUrlTrainViolationPoint(String startTime, String endTime) {
        return "http://27.128.164.147:9600/WebExternal/getViolationPointData.do?"
                + credential
                + "&startTime="
                + startTime
                + "&endTime="
                + endTime;
    }

    /**
     * 4.机车违规项点表json转换
     *
     * @param jsonObject
     * @return
     */
    public static TrainViolationPoint jsonToTrainViolationPoint(com.alibaba.fastjson.JSONObject jsonObject) {
        return JsonUtil.toBeanNoCase(jsonObject, TrainViolationPoint.class);
    }

}
