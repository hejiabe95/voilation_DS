package com.zc.intf.service;

import com.alibaba.fastjson.JSONObject;
import com.zc.intf.entity.TrainViolationFile;

import java.io.Serializable;
import java.text.ParseException;
import java.util.List;

/**
 * 通用
 * 数据同步抽象接口
 * @param <EntityClass>
 */
public interface DSServiceInterface<EntityClass extends Serializable> {

    /**
     * 执行同步
     * @throws ParseException
     */
    void doDS() throws ParseException;

    /**
     * 将respond jsonString 录入DB
     * @param jsonString
     */
    void insertJsonStringtoDatabase(String jsonString);

    /**
     * 将JsonObject中的list转化为entity list
     * @param list
     * @param jsonObject
     */
    void setJsonToEntityList(List<EntityClass> list, JSONObject jsonObject);

    /**
     * jsonObject转化为持久层Entity
     * @param jsonObject
     * @return
     */
    public EntityClass jsonToEntity(JSONObject jsonObject);


    /**
     * 获得 request URL with params
     * @param params
     * @return
     */
    String getUrl(String... params);











}
