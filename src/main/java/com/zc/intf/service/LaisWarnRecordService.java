package com.zc.intf.service;

import com.alibaba.fastjson.JSONObject;
import com.zc.intf.entity.LaisWarnRecord;
import com.zc.intf.entity.TrainViolationFile;
import com.zc.intf.mapper.LaisWarnRecordMapper;
import com.zc.intf.util.CoreHttpUtils;
import com.zc.intf.util.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 73802
 */
@Service
public class LaisWarnRecordService {

    private static final Logger log = LoggerFactory.getLogger(LaisWarnRecordService.class);

    @Resource
    LaisWarnRecordMapper laisWarnRecordMapper;

    public void doDS() {
        log.info("开始同步 train_fault_record ！！");

        String id = laisWarnRecordMapper.selectMaxId();
        if (StringUtils.isEmpty(id)){
            id = "991798";
        }

        String key = System.currentTimeMillis()/5+"";

        //请求地址处理
        String url = getUrlLaisWarnRecord(id,key);
        //调用接口
        String jsonRespond = CoreHttpUtils.post(url, null);

        try {
            insert(jsonRespond);
        } catch (HttpException e) {
            log.error(e.getMessage() + "  url:  " + url);
        }

        log.info(" pcs_train_violation_file 同步完成！！");

    }

    private void insert(String allinfo) throws HttpException {

        List<LaisWarnRecord> dataList = new ArrayList<>();

        //如果没有返回就不处理了
        if (null == allinfo) {
            return;
        }

        //把返回值转换成json
        JSONObject jsonObject = JSONObject.parseObject(allinfo);

        String status = jsonObject.getString("status");
        if (!"success".equals(status)){
            throw new HttpException("数据同步请求接口失败");
        }

        setLaisWarnRecord(dataList, jsonObject);

        dataList.forEach(laisWarnRecord -> {
            try {
                laisWarnRecordMapper.insert(laisWarnRecord);
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
     * 盹睡预警数据json转换
     *
     * @param list
     * @param jsonObject
     */
    private void setLaisWarnRecord(List<LaisWarnRecord> list, JSONObject jsonObject) {

        for (Object info : jsonObject.getJSONArray("datas")) {
            JSONObject object = (JSONObject) info;
            LaisWarnRecord laisWarnRecord = jsonToLaisWarnRecord(object);
            list.add(laisWarnRecord);
        }
    }

    /**
     * 朔黄铁路LAIS数据接口地址
     *
     * @param id
     * @param key
     * @return
     */
    public String getUrlLaisWarnRecord(String id, String key) {
        return "http://218.206.94.236:18086/cmd/newpointtimealarm/getAlarmListByShtl.do?ID="
                + id
                + "&key="
                + key;
    }

    /**
     * 盹睡数据json转换
     *
     * @param jsonObject
     * @return
     */
    public LaisWarnRecord jsonToLaisWarnRecord(JSONObject jsonObject) {
        return JsonUtil.toBeanNoCase(jsonObject, LaisWarnRecord.class);
    }

}
