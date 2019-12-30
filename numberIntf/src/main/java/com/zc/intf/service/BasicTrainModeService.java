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

import com.zc.intf.entity.BasicTrainMode;
import com.zc.intf.mapper.BasicTrainModeMapper;

import javax.annotation.Resource;

import static com.zc.intf.util.DSUtil.*;

@Component
public class BasicTrainModeService {

	private static final Logger log = LoggerFactory.getLogger(BasicTrainModeService.class);
	
	@Resource
	BasicTrainModeMapper basicTrainModeMapper;
	
	@Transactional(rollbackFor = Exception.class)
	public void batchInsert(List<BasicTrainMode> basicTrainModeList){
		basicTrainModeMapper.batchInsert(basicTrainModeList);
	}


	@Transactional(rollbackFor = Exception.class)
	public void deleteALL(){
		basicTrainModeMapper.deleteALL();
	}

	public void doDS() {
		log.info(" 开始同步 pcs_basic_train_mode！！");

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
			deleteALL();
			batchInsert(list);
		}

		log.info(" pcs_basic_train_mode 同步完成！！");
	}

	/**
	 * 3.机车模式码表URL
	 * @return
	 */
	public static String getUrlBasicTrainMode(){
		return "http://27.128.164.147:9600/WebExternal/getAllModeEntity.do?"+credential;
	}

	/**
	 * 3.机车模式码表JSON转换
	 * @param object
	 * @return
	 */
	public static BasicTrainMode jsonToBasicTrainMode(JSONObject object){

		BasicTrainMode basicTrainMode = new BasicTrainMode();
		int id = object.getInt("id");
		basicTrainMode.setId(id);
		String modeName = optString(object,"modeName");
		basicTrainMode.setModeName(modeName);
		int modeMinTotal = object.getInt("modeMinTotal");
		basicTrainMode.setModeMinTotal(modeMinTotal);
		int modeMaxTotal = object.getInt("modeMaxTotal");
		basicTrainMode.setModeMaxTotal(modeMaxTotal);
		String modeSsx = optString(object,"modeSsx");
		basicTrainMode.setModeSsx(modeSsx);
		String modeTrainTypes = optString(object,"modeTrainTypes");
		basicTrainMode.setModeTrainTypes(modeTrainTypes);
		int modeMinCarNum = object.getInt("modeMinCarNum");
		basicTrainMode.setModeMinCarNum(modeMinCarNum);
		int modeMaxCarNum = object.getInt("modeMaxCarNum");
		basicTrainMode.setModeMaxCarNum(modeMaxCarNum);
		int modemintrainID = object.getInt("modeMinTrainID");
		basicTrainMode.setModemintrainID(modemintrainID);
		int modemaxtrainID = object.getInt("modeMaxTrainNum");
		basicTrainMode.setModemaxtrainID(modemaxtrainID);
		int modeMinTrainNum = object.getInt("modeMinTrainNum");
		basicTrainMode.setModeMinTrainNum(modeMinTrainNum);
		int modeMaxTrainNum = object.getInt("modeMaxTrainNum");
		basicTrainMode.setModeMaxTrainNum(modeMaxTrainNum);
		return basicTrainMode;
	}
 
}
