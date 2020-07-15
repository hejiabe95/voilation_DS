package com.zc.intf.service;

import com.alibaba.fastjson.JSONObject;
import com.zc.intf.entity.WeatherWarn;
import com.zc.intf.entity.WeatherWarnRsp;
import com.zc.intf.mapper.WeatherWarnMapper;
import com.zc.intf.util.CoreHttpUtils;
import com.zc.intf.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class WeatherWarnService {

    private static final Logger log = LoggerFactory.getLogger(WeatherWarnService.class);

    @Resource
    WeatherWarnMapper weatherWarnMapper;


    @Async
    public void doDS() {

        log.info("开始同步 wealther_warn ！！");

        String url = "";

        String jsonRespond = "";

        Date currentTime = new Date();
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMdd");

        SimpleDateFormat formatHour = new SimpleDateFormat("HH");

        String date = formatDate.format(currentTime);
        String hour = formatHour.format(currentTime);
        url = getUrlWeatherWarn(date, hour);
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
        WeatherWarnRsp weatherWarnRsp = JsonUtil.toBeanNoCase(jsonObject, WeatherWarnRsp.class);

        String datetime = weatherWarnRsp.getDatetime();

        String thour = weatherWarnRsp.getThour();

        Map<String, List<WeatherWarn>> map = weatherWarnRsp.getData();
        for (Map.Entry<String, List<WeatherWarn>> entry : map.entrySet()) {
            //构建对象
            WeatherWarn weatherWarn = entry.getValue().get(0);
            //判断数据是否重复(通过warnId和sign预警状态)
            List<WeatherWarn> list = weatherWarnMapper.selectRepeat(weatherWarn);
            if (list.size() > 0) {
                continue;
            }
            weatherWarn.setReadHour(thour);
            weatherWarn.setReadTime(datetime);
            weatherWarn.setCreateTime(currentTime);
            try {
                weatherWarnMapper.insert(weatherWarn);
            } catch (Exception e) {
                if (e.getMessage().contains("ORA-00001")) {
                    log.info(e.getMessage());
                } else {
                    log.error(e.getMessage());
                }
            }
        }

    }


    /**
     * 天气预警URL
     *
     * @param hour
     * @return
     */
    public static String getUrlWeatherWarn(String date, String hour) {
        return "http://api.weatheryun.com/api/getdata.action?user=shuohuang02&token=B406F0ECC0D6B23AFCB335AAD118FEB2&type=warn&date="
                + date
                + "&thour="
                + hour;
    }


}
