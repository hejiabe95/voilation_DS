package com.zc.intf.service;

import com.alibaba.fastjson.JSONObject;
import com.zc.intf.entity.StationLive;
import com.zc.intf.entity.StationWeatherForecast;
import com.zc.intf.entity.StationWeatherRsp;
import com.zc.intf.entity.TrainViolationPoint;
import com.zc.intf.mapper.StationLiveMapper;
import com.zc.intf.mapper.StationWeatherMapper;
import com.zc.intf.util.CoreHttpUtils;
import com.zc.intf.util.JsonUtil;
import org.apache.http.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class StationLiveService {

    private static final Logger log = LoggerFactory.getLogger(StationLiveService.class);

    @Resource
    StationLiveMapper stationLiveMapper;


    //stationids表示站的信息，all代表用户关注的所有站，stationids可以为B1951,B1952，站号中间用,隔开
    static final String stationids = "all";

    @Async
    public void doDS() {

        log.info("开始同步 station_live ！！");
        String url = "";
        String jsonRespond = "";

        Date currentTime = new Date();
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMddHHmm");
        String enddate = formatDate.format(currentTime);
        //获取分钟个位数
        int mm = Integer.parseInt(enddate.substring(11, 12));
        int minute = 0 ;
        if (mm == 0 || mm == 5) {
            minute = 0;
        } else if (mm > 0 && mm < 5) {
            minute = mm - 0;
        } else {
            minute = mm - 5;
        }
        //因为数据延时推送，往前推5分钟同步
        minute = minute + 5;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentTime);
        calendar.add(Calendar.MINUTE, - minute);
        //获取同步的时间节点
        String readtime = formatDate.format(calendar.getTime());
        //获取数据库最新同步数据的时间节点
        String maxReadtime = stationLiveMapper.selectMaxTime();
        //如果数据库最新同步数据的时间节点 与 当前统计节点一直 ，则放掉该次定时
        if (readtime.equals(maxReadtime)){
            return;
        }
        url = getUrlWeatherWarn(readtime);
        //调用接口
        jsonRespond = CoreHttpUtils.get(url, null);
        insertJsonStringtoDatabase(jsonRespond, currentTime, readtime);

        log.info(" station_live 同步完成！！");

    }

    private void insertJsonStringtoDatabase(String allinfo, Date currentTime, String readtime) {
        List<StationLive> dataList = new ArrayList<>();
        //如果没有返回就不处理了
        if (null == allinfo) {
            return;
        }
        //把返回值转换成json
        JSONObject jsonObject = JSONObject.parseObject(allinfo);

        setTrainViolationPoint(dataList, jsonObject);

        dataList.forEach(trainViolationPoint -> {
            try {
                trainViolationPoint.setReadTime(readtime);
                trainViolationPoint.setCreateTime(currentTime);
                stationLiveMapper.insert(trainViolationPoint);
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
    private void setTrainViolationPoint(List<StationLive> list, JSONObject jsonObject) {

        for (Object info : jsonObject.getJSONArray("data")) {
            JSONObject object = (JSONObject) info;
            list.add(jsonToTrainViolationPoint(object));
        }
    }

    /**
     * 4.机车违规项点表json转换
     *
     * @param jsonObject
     * @return
     */
    public static StationLive jsonToTrainViolationPoint(com.alibaba.fastjson.JSONObject jsonObject) {
        return JsonUtil.toBeanNoCase(jsonObject, StationLive.class);
    }

    /**
     * 站点天气
     *
     * @param date
     * @return
     */
    public static String getUrlWeatherWarn(String date) {
        return  "http://api.weatheryun.com/api/getdata.action?user=shuohuang02&token=B406F0ECC0D6B23AFCB335AAD118FEB2&type=railwayobserve&stationids="
                + stationids
                + "&date="
                + date;
    }


}
