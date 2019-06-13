package com.zc.intf.task;

import java.util.ArrayList;
import java.util.List;

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

import net.sf.json.JSONArray;
  

@Component
public class TrainTask {
	
    @Autowired
    private TrainNumberService trainNumberService;
    
    @Autowired
    private BasicTrainTeamService basicTrainTeamService;
    
    @Autowired
    private BasicTrainModeService basicTrainModeService;
	 /**
     * 每分钟更新
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void getTrainNumberViolation() {
    	String allinfo = CoreHttpUtils.post("http://27.128.164.147:9600/WebExternal/getFileBeanByParam.do?teamID&modeID&startTime=2019-04-01&endTime=2019-04-02&trainNum&trainID&start&length", null);
  
    	List<TrainNumberViolation> list = new ArrayList<TrainNumberViolation>(); 
      
    	JSONArray json = JSONArray.fromObject(allinfo);
   
    	//list = (List<TrainNumberViolation>) JSONArray.toCollection(json,TrainNumberViolation.class);
    	TrainNumberViolation bb = new TrainNumberViolation();
    	bb.setFileId(2);
    	list.add(bb); 
    	
    	
    	TrainNumberViolation aa = new TrainNumberViolation();
    	aa.setFileId(1);
    	list.add(aa); 
    	trainNumberService.batchInsert(list);
    	
    	System.out.println("成功"); 
      
    }

    
    
    @Scheduled(cron = "0 0/1 * * * ?")
    public void getBasicTrainTeam() {
    	String allinfo = CoreHttpUtils.post("http://27.128.164.147:9600/WebExternal/getAllTeam.do", null);
  
    	List<BasicTrainTeam> list = new ArrayList<BasicTrainTeam>(); 
      
    	JSONArray json = JSONArray.fromObject(allinfo);
   
    	//list = (List<TrainNumberViolation>) JSONArray.toCollection(json,TrainNumberViolation.class);
    	
    	BasicTrainTeam vv = new BasicTrainTeam();
    	vv.setId(6);
    	list.add(vv); 
    	
    	BasicTrainTeam aa = new BasicTrainTeam();
    	aa.setId(7);
    	list.add(aa); 
    	basicTrainTeamService.deleteALL();
    	basicTrainTeamService.batchInsert(list);
    	
    	System.out.println("成功"); 
      
    }
    
    
    @Scheduled(cron = "0 0/1 * * * ?")
    public void getBasicTrainMode() {
    	String allinfo = CoreHttpUtils.post("http://27.128.164.147:9600/WebExternal/getAllModeEntity.do", null);
  
    	List<BasicTrainMode> list = new ArrayList<BasicTrainMode>(); 
      
    	JSONArray json = JSONArray.fromObject(allinfo);
   
    	//list = (List<TrainNumberViolation>) JSONArray.toCollection(json,TrainNumberViolation.class);
    	BasicTrainMode vv = new BasicTrainMode();
    	vv.setId(4);
    	list.add(vv); 
    	
    	
    	BasicTrainMode aa = new BasicTrainMode();
    	aa.setId(5);
    	list.add(aa); 
    	basicTrainModeService.deleteALL();
    	basicTrainModeService.batchInsert(list);
    	
    	System.out.println("成功"); 
      
    }

}







