package com.zc.intf.service;

import com.alibaba.fastjson.JSONObject;
import com.zc.intf.entity.TrainNumberViolation;
import com.zc.intf.entity.TrainViolationFile;
import com.zc.intf.mapper.TrainViolationFileMapper;
import com.zc.intf.util.CoreHttpUtils;
import com.zc.intf.util.DSUtil;
import com.zc.intf.util.JsonUtil;
import org.apache.http.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.xml.ws.http.HTTPException;
import java.security.PrivateKey;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.zc.intf.util.DSUtil.*;

/**
 * @author 73802
 */
@Service
public class TrainViolationFileService {

    private static final Logger log = LoggerFactory.getLogger(TrainViolationFileService.class);

    @Resource
    TrainViolationFileMapper trainViolationFileMapper;


    public void doDS() throws ParseException {
        log.info("开始同步 pcs_train_violation_file ！！");

        String url = "";

        String jsonRespond = "";

        String startTime = "";

        String endTime = "";

        //获取之前同步的最后时间作为开始时间
        String lodNewTime = trainViolationFileMapper.queryNewTime();

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
                url = getUrlTrainViolationFile(loopStartTime, loopEndTime);
                //调用接口
                jsonRespond = CoreHttpUtils.post(url, null);
                try {
                    insertJsonStringtoDatabase(jsonRespond);
                } catch (HttpException e) {
                    log.error(e.getMessage() + "  url:  " + url);
                }

            }

            //余数处理
            url = getUrlTrainViolationFile(getTimePlus(loopEndTime, 1),
                    getTimePlus(loopEndTime, 1 + remainder));
            //调用接口
            jsonRespond = CoreHttpUtils.post(url, null);
            try {
                insertJsonStringtoDatabase(jsonRespond);
            } catch (HttpException e) {
                log.error(e.getMessage() + "  url:  " + url);
            }

        } else {

            url = getUrlTrainViolationFile(startTime, endTime);
            //调用接口
            jsonRespond = CoreHttpUtils.post(url, null);
            try {
                insertJsonStringtoDatabase(jsonRespond);
            } catch (HttpException e) {
                log.error(e.getMessage() + "  url:  " + url);
            }
        }


        log.info(" pcs_train_violation_file 同步完成！！");

    }

    private void insertJsonStringtoDatabase(String allinfo) throws HttpException {

        List<TrainViolationFile> dataList = new ArrayList<>();

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

        setTrainViolationFile(dataList, jsonObject);

        dataList.forEach(trainViolationFile -> {
            try {
                trainViolationFileMapper.insert(trainViolationFile);
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
     * 机车违规文件表json转换
     *
     * @param list
     * @param jsonObject
     */
    private void setTrainViolationFile(List<TrainViolationFile> list, JSONObject jsonObject) {

        for (Object info : jsonObject.getJSONArray("data")) {
            JSONObject object = (JSONObject) info;
            TrainViolationFile trainViolationFile = jsonToTrainViolationFile(object);
            getTrainType(trainViolationFile);
            list.add(trainViolationFile);
        }
    }

    /**
     * 通过车号统计列车类型
     * @param trainViolationFile
     */
    public void getTrainType(TrainViolationFile trainViolationFile){
        String trainNo = trainViolationFile.getTrainnumber().toString();
        if (null != trainNo && trainNo.length() == 5){
            //C64万吨列车（五位车号前两位为16）
            if ("16".equals(trainNo.substring(0,2))){
                trainViolationFile.setTraintype("C64万吨列车");
            }
            //两万吨列车（五位数开头1位为2的）
            else if ("2".equals(trainNo.substring(0,1))){
                trainViolationFile.setTraintype("两万吨列车");
            }
            //万吨列车（五位车号开头1位为1且第二位不为6）
            else if ("1".equals(trainNo.substring(0,1)) && !"6".equals(trainNo.substring(1,2))){
                trainViolationFile.setTraintype("万吨列车");
            }
            else {
                trainViolationFile.setTraintype("普通列车");
            }
        }
        else {
            trainViolationFile.setTraintype("普通列车");
        }
    }


    /**
     * 4.机车违规文件表URL
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public String getUrlTrainViolationFile(String startTime, String endTime) {
        return "http://27.128.164.147:9600/WebExternal/getViolationFileData.do?"
                + credential
                + "&startTime="
                + startTime
                + "&endTime="
                + endTime;
    }

    /**
     * 4.机车违规文件表json转换
     *
     * @param jsonObject
     * @return
     */
    public TrainViolationFile jsonToTrainViolationFile(JSONObject jsonObject) {
        return JsonUtil.toBeanNoCase(jsonObject, TrainViolationFile.class);
    }

}
