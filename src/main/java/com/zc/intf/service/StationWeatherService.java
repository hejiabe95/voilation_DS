package com.zc.intf.service;

import com.alibaba.fastjson.JSONObject;
import com.zc.intf.entity.StationWeatherForecast;
import com.zc.intf.entity.StationWeatherRsp;
import com.zc.intf.entity.WeatherWarn;
import com.zc.intf.entity.WeatherWarnRsp;
import com.zc.intf.mapper.StationWeatherMapper;
import com.zc.intf.mapper.WeatherWarnMapper;
import com.zc.intf.util.CoreHttpUtils;
import com.zc.intf.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class StationWeatherService {

    private static final Logger log = LoggerFactory.getLogger(StationWeatherService.class);

    @Resource
    StationWeatherMapper stationWeatherMapper;

    //表示请求的时效，固定为20
    static final String thour = "20";

    //stationids表示站的信息，all代表用户关注的所有站，stationids可以为B1951,B1952，站号中间用,隔开
    static final String stationids = "all";

    @Async
    public void doDS() {

        log.info("开始同步 station_weather ！！");
        String url = "";
        String jsonRespond = "";

        Date currentTime = new Date();
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMdd");

        String date = formatDate.format(currentTime);
        url = getUrlWeatherWarn(date);
        //调用接口
        jsonRespond = CoreHttpUtils.get(url, null);
        insertJsonStringtoDatabase(jsonRespond, currentTime);

        log.info(" pcs_train_violation_point 同步完成！！");

    }

    private void insertJsonStringtoDatabase(String allinfo, Date currentTime) {
        //如果没有返回就不处理了
        if (null == allinfo) {
            return;
        }
        //把返回值转换成json
        JSONObject jsonObject = JSONObject.parseObject(allinfo);
        StationWeatherRsp stationWeatherRsp = JsonUtil.toBeanNoCase(jsonObject, StationWeatherRsp.class);

        String datetime = stationWeatherRsp.getDatetime();

        String thour = stationWeatherRsp.getThour();

        Map<String, Map<String, StationWeatherForecast>> map = stationWeatherRsp.getData();
        for (Map.Entry<String, Map<String, StationWeatherForecast>> entry : map.entrySet()) {
            //站点编号
            String station_code = entry.getKey();
            Map<String, StationWeatherForecast> timeMap = entry.getValue();
            for (Map.Entry<String, StationWeatherForecast> timeEntry : timeMap.entrySet()) {
                String future_hour = timeEntry.getKey();
                StationWeatherForecast value = timeEntry.getValue();
                value.setReadTime(datetime);
                value.setStation_code(station_code);
                value.setFuture_hour(future_hour);
                value.setValidHour(thour);
                value.setCreateTime(currentTime);
                //防重
                List<StationWeatherForecast> stationWeatherForecasts = stationWeatherMapper.selectRepeat(value);
                if (stationWeatherForecasts.size() > 0) {
                    continue;
                }
                stationWeatherMapper.insert(value);
            }
        }
    }


    /**
     * 站点天气
     *
     * @param date
     * @return
     */
    public static String getUrlWeatherWarn(String date) {
        return "http://api.weatheryun.com/api/getdata.action?user=shuohuang02&token=B406F0ECC0D6B23AFCB335AAD118FEB2&type=station7dforcast&date="
                + date
                + "&thour="
                + thour
                + "&stationids="
                + stationids;
    }


}
