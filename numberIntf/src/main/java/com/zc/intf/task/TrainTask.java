package com.zc.intf.task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;
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
  

@Component
public class TrainTask {
	
    @Autowired
    private TrainNumberService trainNumberService;
    
    @Autowired
    private BasicTrainTeamService basicTrainTeamService;
    
    @Autowired
    private BasicTrainModeService basicTrainModeService;
	 /**
     * 每小时更新
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void getTrainNumberViolation()  {
    	//一个小时一次
    	
    	String url = "";
    	String lodNewTime = trainNumberService.quertyNewTime();//获取之前同步的最后时间作为开始时间
    	
    	//如果非第一次同步就就用老数据的最晚时间
    	if (null != lodNewTime && !"".equals(lodNewTime))
    	{
    		 url = "http://27.128.164.147:9600/WebExternal/getFileBeanByParam.do?teamID&modeID&startTime="+lodNewTime+"&endTime="+getTime()+"&trainNum&trainID&start=0&length=100";
    	}
    	else
    	{
    		//第一次同步就用当前时间减一个小时
    	     url = "http://27.128.164.147:9600/WebExternal/getFileBeanByParam.do?teamID&modeID&startTime="+getTimejianyi()+"&endTime="+getTime()+"&trainNum&trainID&start=0&length=100";
    	}
    	url=url.replaceAll(" ", "%20");//时间有空格需要转义
    	String allinfo = CoreHttpUtils.post(url, null);//调用接口
    	 
        if (null  == allinfo)//如果没有返回就不处理了
        {
        	return;
        }
        JSONObject jsonObject = new JSONObject(allinfo);//把返回值转换成json
        
        int code =  jsonObject.getInt("code");//获取状态码
        String msg = jsonObject.getString("msg");//获取信息
        //不成功记录下来
        if (1 != code)
        {
        	System.out.println("syn data is error, msg ========" + msg);
        	return;
        }
        
        int resultNum =  jsonObject.getInt("resultNum");//获取状态码
        
        //如果大于100条需要分批次处理
        if (resultNum > 100)
        {
        	for (int i = 0 ;i < resultNum ;i=i+100)
        	{
            	List<TrainNumberViolation> list = new ArrayList<TrainNumberViolation>(); 
                String url1 = "";
              	if (null != lodNewTime && !"".equals(lodNewTime))
            	{
              		url1 = "http://27.128.164.147:9600/WebExternal/getFileBeanByParam.do?teamID&modeID&startTime="+lodNewTime+"&endTime="+getTime()+"&trainNum&trainID&start="+i+"&length=100";
            	}
              	else
              	{
              		url1 = "http://27.128.164.147:9600/WebExternal/getFileBeanByParam.do?teamID&modeID&startTime="+getTimejianyi()+"&endTime="+getTime()+"&trainNum&trainID&start="+i+"&length=100";
              	}
              	url1=url1.replaceAll(" ", "%20");//时间有空格需要转义
              	
            	String allinfo1 = CoreHttpUtils.post(url1, null);//调用接口
            	
            	JSONObject jsonObject1 = new JSONObject(allinfo1);//把返回值转换成json
            	//循环数据
                setTrainNumberViolation(list, jsonObject1);
     
                //有数据就入库
                if (null != list && list.size() > 0 )
                {
             	  	trainNumberService.batchInsert(list);
                }
        	}
        	 
        }
        else
        {
        	List<TrainNumberViolation> list = new ArrayList<TrainNumberViolation>(); 
        	
        	setTrainNumberViolation(list, jsonObject);
            //有数据就入库
            if (null != list && list.size() > 0 )
            {
         	  	trainNumberService.batchInsert(list);
            }
        }
    	System.out.println("成功"); 
    }


    /**
     * 把JSON转换成值对象
     * @param list
     * @param jsonObject1
     */
	private void setTrainNumberViolation(List<TrainNumberViolation> list, JSONObject jsonObject1) {
		
		for (Object info : jsonObject1.getJSONArray("data")) {
		     JSONObject object = (JSONObject)info;
		     String fileId = optString(object,"fileId");
		     int count = trainNumberService.quertyByFileId(fileId);
		     //已经存在的不入库
		     if (count > 0)
		     {
		     	continue;
		     }
		     TrainNumberViolation trainNumberViolation = new TrainNumberViolation();
		     trainNumberViolation.setFileId(fileId);
		     String filePath = optString(object,"filePath");
		     trainNumberViolation.setFilePath(filePath);
		     int number = object.getInt("number");
		     trainNumberViolation.setNumber(number);
		     String departureTime = optString(object,"departureTime");
		     trainNumberViolation.setDepartureTime(departureTime);
		     String departureTimems = optString(object,"departureTimems");
		     trainNumberViolation.setDepartureTimems(departureTimems);
		     int trainNum = object.getInt("trainNum");
		     trainNumberViolation.setTrainNum(trainNum);
		     String trainId = optString(object,"trainId");
		     trainNumberViolation.setTrainId(trainId);
		     String driverName = optString(object,"driverName");
		     trainNumberViolation.setDriverName(driverName);
		     String viceDriverName = optString(object,"viceDriverName");
		     trainNumberViolation.setViceDriverName(viceDriverName);
		     int roadId = object.getInt("roadId");
		     trainNumberViolation.setRoadId(roadId);
		     int total = object.getInt("total");
		     trainNumberViolation.setTotal(total);
		     int length = object.getInt("length");
		     trainNumberViolation.setLength(length);
		     String departureStation = optString(object,"departureStation");
		     trainNumberViolation.setDepartureStation(departureStation);
		     String passengerFreight = optString(object,"passengerFreight");
		     trainNumberViolation.setPassengerFreight(passengerFreight);
		     String trainType =  optString(object,"trainType");
		     trainNumberViolation.setTrainType(trainType);
		     int violationNum = object.getInt("violationNum");
		     trainNumberViolation.setViolationNum(violationNum);
		     int deductMarks = object.getInt("deductMarks");
		     trainNumberViolation.setDeductMarks(deductMarks);
		     int score = object.getInt("score");
		     trainNumberViolation.setScore(score);
		     String thisIt = optString(object,"thisIt");
		     trainNumberViolation.setThisIt(thisIt);
		     String violationRemark = object.getString("violationRemark");
		     trainNumberViolation.setViolationRemark(violationRemark);
		     String modeID = optString(object,"modeID");
		     trainNumberViolation.setModeID(modeID);
		     String modeName = optString(object,"modeName");
		     trainNumberViolation.setModeName(modeName);
		     String teamID = optString(object,"teamID");
		     trainNumberViolation.setTeamID(teamID);
		     String teamName = optString(object,"teamName");
		     trainNumberViolation.setTeamName(teamName);
		     list.add(trainNumberViolation);
		   
		 }
	}

    
    
    @Scheduled(cron = "0 0 * * * ?")
    public void getBasicTrainTeam() {
    	//调养接口获取信息
    	String allinfo = CoreHttpUtils.post("http://27.128.164.147:9600/WebExternal/getAllTeam.do", null);
        //返回为空就不处理了
    	if (null  == allinfo)
        {
        	return;
        }
    	List<BasicTrainTeam> list = new ArrayList<BasicTrainTeam>(); 
        //解析json
        JSONObject jsonObject = new JSONObject(allinfo);
        
        int code =  jsonObject.getInt("code");
        String msg = jsonObject.getString("msg");
        //通过返回码判断是否成功
        if (1 != code)
        {
        	System.out.println("syn data is error, msg ========" + msg);
        	return;
        }
    
        //数据了非常小，整成一个值对象
       for (Object info : jsonObject.getJSONArray("data")) {
            JSONObject object = (JSONObject)info;
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
            list.add(basicTrainTeam);
       }
  
    	
       //先删后增
       if (null != list && list.size() > 0)
       {
    		basicTrainTeamService.deleteALL();
        	basicTrainTeamService.batchInsert(list);
       }
    
    	
    	System.out.println("getBasicTrainTeam 同步成功"); 
      
    }
    
    
    @Scheduled(cron = "0 0 * * * ?")
    public void getBasicTrainMode() {
    	//调用接口得到数据
    	String allinfo = CoreHttpUtils.post("http://27.128.164.147:9600/WebExternal/getAllModeEntity.do", null);
        //为空就不处理了
    	if (null  == allinfo)
        {
        	return;
        }
    	
    	List<BasicTrainMode> list = new ArrayList<BasicTrainMode>(); 
        //解析JSON
        JSONObject jsonObject = new JSONObject(allinfo);
        
        int code =  jsonObject.getInt("code");
        String msg = jsonObject.getString("msg");
        //通过返回码判断是否成功
        if (1 != code)
        {
        	System.out.println("syn data is error, msg ========" + msg);
        	return;
        }
    

        //整成值对象
       for (Object info : jsonObject.getJSONArray("data")) {
    	   JSONObject object = (JSONObject)info;
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
           list.add(basicTrainMode);
    	   
       }
 
       //先删后增
    	if (null != list && list.size() > 0)
        {
	    	basicTrainModeService.deleteALL();
	    	basicTrainModeService.batchInsert(list);
        }
    	
    	System.out.println("getBasicTrainMode 同步成功"); 
      
    }
    
    /**
     * 防止 空的JSON字段
     * @param json
     * @param key
     * @return
     */
    public  String optString(JSONObject json, String key) {
        if (json.isNull(key)) {
            return null;
        } else {
            return json.optString(key, null);
        }
    }
 

    /**
     * 获取当前时间，年月日
     * @return
     */
	public  String getTime() {
		SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd");
		return sdfTime.format(new Date());
	}
	
	/**
	 * 获取当前时间减去一个小时
	 * @return
	 */
	public  String getTimejianyi() {
		Calendar calen = Calendar.getInstance();//可以对每个时间域单独修改
		calen.setTime(new Date());
		calen.set(Calendar.HOUR_OF_DAY, calen.get(Calendar.HOUR_OF_DAY) - 1);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH");
		String time = format.format(calen.getTime());
		return time;
	}
	
	
}







