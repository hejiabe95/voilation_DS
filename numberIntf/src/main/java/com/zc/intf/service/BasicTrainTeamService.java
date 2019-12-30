package com.zc.intf.service;

import java.util.ArrayList;
import java.util.List;

import com.zc.intf.util.CoreHttpUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.zc.intf.entity.BasicTrainTeam;
import com.zc.intf.mapper.BasicTrainTeamMapper;

import javax.annotation.Resource;

import static com.zc.intf.util.DSUtil.*;

@Component
public class BasicTrainTeamService {

    private static final Logger log = LoggerFactory.getLogger(BasicTrainTeamService.class);

    @Resource
    BasicTrainTeamMapper basicTrainTeamMapper;

    @Transactional(rollbackFor = Exception.class)
    public void batchInsert(List<BasicTrainTeam> basicTrainTeamList) {
        basicTrainTeamMapper.batchInsert(basicTrainTeamList);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteALL() {
        basicTrainTeamMapper.deleteALL();
    }

    public void doDS() {
        log.info(" 开始同步 pcs_basic_train_team！！");

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
            deleteALL();
            batchInsert(list);
        }

        log.info(" pcs_basic_train_team 同步完成！！");
    }

	/**
	 * 2.机车班组码表URL
	 * @return
	 */
	public static String getUrlBasicTrainTeam(){
		return "http://27.128.164.147:9600/WebExternal/getAllTeam.do?"+credential;
	}

	/**
	 * 2.机车班组码表JSON转换
	 * @param object
	 * @return
	 */
	public static BasicTrainTeam jsonToBasicTrainTeam(JSONObject object){

		BasicTrainTeam basicTrainTeam = new BasicTrainTeam();
		int number = object.getInt("number");
		basicTrainTeam.setNumber(number);
		int id = object.getInt("id");
		basicTrainTeam.setId(id);
		String teamName = optString(object,"teamName");
		basicTrainTeam.setTeamName(teamName);
		String teamNote = optString(object,"teamNote");
		basicTrainTeam.setTeamNote(teamNote);
		String teamParentID = optString(object,"teamParentID");
		basicTrainTeam.setTeamParentID(teamParentID);
		return basicTrainTeam;
	}
}
