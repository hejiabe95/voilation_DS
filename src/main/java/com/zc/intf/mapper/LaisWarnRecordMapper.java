package com.zc.intf.mapper;

import com.zc.intf.entity.LaisWarnRecord;

public interface LaisWarnRecordMapper {
    String selectMaxId();

    int insert(LaisWarnRecord record);

    LaisWarnRecord selectByPrimaryKey(String id);

}