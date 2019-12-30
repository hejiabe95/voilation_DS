package com.zc.intf.service;

import com.alibaba.fastjson.JSONObject;
import com.zc.intf.entity.TrainViolationFile;
import com.zc.intf.util.JsonUtil;

import java.io.Serializable;
import java.text.ParseException;
import java.util.List;

public abstract class AbstractDSService <EntityClass extends Serializable> implements DSServiceInterface{

    @Override
    public void doDS() throws ParseException {

    }

    @Override
    public void insertJsonStringtoDatabase(String jsonString) {

    }

    @Override
    public void setJsonToEntityList(List list, JSONObject jsonObject) {

    }

    @Override
    public EntityClass jsonToEntity(JSONObject jsonObject) {
        return null;
    }

    @Override
    public abstract String getUrl(String... params);
}
