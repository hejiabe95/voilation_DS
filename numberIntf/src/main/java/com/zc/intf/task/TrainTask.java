package com.zc.intf.task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.zc.intf.util.DSUtil;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.zc.intf.entity.BasicTrainMode;
import com.zc.intf.entity.BasicTrainTeam;
import com.zc.intf.entity.TrainNumberViolation;
import com.zc.intf.service.BasicTrainModeService;
import com.zc.intf.service.BasicTrainTeamService;
import com.zc.intf.service.TrainNumberService;
import com.zc.intf.util.CoreHttpUtils;

import javax.annotation.PostConstruct;

import static com.zc.intf.util.DSUtil.*;


@Component
public class TrainTask {

    private static final Logger logger = LoggerFactory.getLogger(TrainTask.class);

    @Autowired
    private TrainNumberService trainNumberService;

    @Autowired
    private BasicTrainTeamService basicTrainTeamService;

    @Autowired
    private BasicTrainModeService basicTrainModeService;


    /**
     * 1.机车得分表数据同步
     * 每小时更新
     */
	@Scheduled(cron = "0 0 * * * ?")
    public void doTrainNumberViolation() {

        logger.info("开始同步 pcs_train_number_violation ！！");

        //一个小时一次

        String url = "";
        String lodNewTime = trainNumberService.quertyNewTime();//获取之前同步的最后时间作为开始时间

        //如果非第一次同步就就用老数据的最晚时间
        if (null != lodNewTime && !"".equals(lodNewTime)) {
            url = getUrlTrainNumberViolaion("2019-08-26 00:00:00", getTime(), 0);
        } else {
            //第一次同步就用当前时间减一个小时
            url = getUrlTrainNumberViolaion(getTimejianyi(), getTime(), 0);
        }

        String allinfo = CoreHttpUtils.post(url, null);//调用接口

        if (null == allinfo)//如果没有返回就不处理了
        {
            return;
        }
        JSONObject jsonObject = new JSONObject(allinfo);//把返回值转换成json

        int code = jsonObject.getInt("code");//获取状态码
        String msg = jsonObject.getString("msg");//获取信息
        //不成功记录下来
        if (1 != code) {
            System.out.println("syn data is error, msg ========" + msg);
            return;
        }

        int resultNum = jsonObject.getInt("resultNum");//获取状态码

        //如果大于100条需要分批次处理
        if (resultNum > 100) {
            for (int i = 0; i < resultNum; i = i + 100) {
                List<TrainNumberViolation> list = new ArrayList<TrainNumberViolation>();
                String url1 = "";
                if (null != lodNewTime && !"".equals(lodNewTime)) {
                    url1 = getUrlTrainNumberViolaion("2019-08-26 00:00:00", getTime(), i);
                } else {
                    url1 = getUrlTrainNumberViolaion(getTimejianyi(), getTime(), i);
                }

                String allinfo1 = CoreHttpUtils.post(url1, null);//调用接口

                JSONObject jsonObject1 = new JSONObject(allinfo1);//把返回值转换成json
                //循环数据
                setTrainNumberViolation(list, jsonObject1);

                //有数据就入库
                if (null != list && list.size() > 0) {
                    trainNumberService.batchInsert(list);
                }
            }

        } else {
            List<TrainNumberViolation> list = new ArrayList<TrainNumberViolation>();

            setTrainNumberViolation(list, jsonObject);
            //有数据就入库
            if (null != list && list.size() > 0) {
                trainNumberService.batchInsert(list);
            }
        }
        logger.info(" pcs_train_number_violation 同步完成！！");
    }


    /**
     * 1.机车评分表json转换
     *
     * @param list
     * @param jsonObject1
     */
    private void setTrainNumberViolation(List<TrainNumberViolation> list, JSONObject jsonObject1) {

        for (Object info : jsonObject1.getJSONArray("data")) {
            JSONObject object = (JSONObject) info;
            String fileId = optString(object, "fileId");
            int count = trainNumberService.quertyByFileId(fileId);
            //已经存在的不入库
            if (count > 0) {
                continue;
            }
            list.add(jsonToTrainNumberViolation(object));

        }
    }


    /**
     * 2.机车班组码表数据同步
     * 每小时更新
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void doBasicTrainTeam() {

        logger.info(" 开始同步 pcs_basic_train_team！！");

        //调接口获取信息
        String allinfo = CoreHttpUtils.post(getUrlBasicTrainTeam(), null);
        //返回为空就不处理了
        if (null == allinfo) {
            return;
        }
        List<BasicTrainTeam> list = new ArrayList<BasicTrainTeam>();
        //解析json
        JSONObject jsonObject = new JSONObject(allinfo);

        int code = jsonObject.getInt("code");
        String msg = jsonObject.getString("msg");
        //通过返回码判断是否成功
        if (1 != code) {
            System.out.println("syn data is error, msg ========" + msg);
            return;
        }

        //数据了非常小，整成一个值对象
        for (Object info : jsonObject.getJSONArray("data")) {
            JSONObject object = (JSONObject) info;
            list.add(jsonToBasicTrainTeam(object));
        }

        //先删后增,全量更新
        if (null != list && list.size() > 0) {
            basicTrainTeamService.deleteALL();
            basicTrainTeamService.batchInsert(list);
        }

        logger.info(" pcs_basic_train_team 同步完成！！");

    }


    @Scheduled(cron = "0 0 * * * ?")
    public void doBasicTrainMode() {

        logger.info(" 开始同步 pcs_basic_train_mode！！");

        //调用接口得到数据
        String allinfo = CoreHttpUtils.post("http://27.128.164.147:9600/WebExternal/getAllModeEntity.do", null);
        //为空就不处理了
        if (null == allinfo) {
            return;
        }

        List<BasicTrainMode> list = new ArrayList<BasicTrainMode>();
        //解析JSON
        JSONObject jsonObject = new JSONObject(allinfo);

        int code = jsonObject.getInt("code");
        String msg = jsonObject.getString("msg");
        //通过返回码判断是否成功
        if (1 != code) {
            System.out.println("syn data is error, msg ========" + msg);
            return;
        }


        //整成值对象
        for (Object info : jsonObject.getJSONArray("data")) {
            JSONObject object = (JSONObject) info;
            list.add(jsonToBasicTrainMode(object));
        }

        //先删后增
        if (null != list && list.size() > 0) {
            basicTrainModeService.deleteALL();
            basicTrainModeService.batchInsert(list);
        }

        logger.info(" pcs_basic_train_mode 同步完成！！");

    }


}







