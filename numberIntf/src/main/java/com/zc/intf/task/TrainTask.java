package com.zc.intf.task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.zc.intf.service.*;
import com.zc.intf.util.DSUtil;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.zc.intf.entity.BasicTrainMode;
import com.zc.intf.entity.BasicTrainTeam;
import com.zc.intf.entity.TrainNumberViolation;
import com.zc.intf.util.CoreHttpUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import static com.zc.intf.util.DSUtil.*;


@Component
public class TrainTask {

    private static final Logger logger = LoggerFactory.getLogger(TrainTask.class);

    @Resource
    private TrainNumberService trainNumberService;

    @Resource
    private BasicTrainTeamService basicTrainTeamService;

    @Resource
    private BasicTrainModeService basicTrainModeService;

    @Resource
    private TrainViolationFileService trainViolationFileService;

    @Resource
    private TrainViolationPointService trainViolationPointService;


    /**
     * 1.机车得分表数据同步
     * 已经弃用
     */
    public void doTrainNumberViolation() throws Exception {
	    trainNumberService.doDS();
    }

    /**
     * 2.机车班组码表数据同步
     * 每小时更新
     */
    @Scheduled(cron = "0 0 0/1 * * ?")
    @Async
    public void doBasicTrainTeam() {
        basicTrainTeamService.doDS();
    }

    /**
     * 3.机车模式码表数据同步
     * 每小时更新
     */
    @Scheduled(cron = "0 0 0/1 * * ?")
    @Async
    public void doBasicTrainMode() {
        basicTrainModeService.doDS();
    }

    /**
     * 4.机车违规文件数据同步
     * 每小时更新
     */
    @Scheduled(cron = "0 0 0/1 * * ?")
    @Async
    public void doTrainViolationFile() throws Exception{
        trainViolationFileService.doDS();
    }

    /**
     * 4.机车违规项点数据同步
     * 每小时更新
     */
    @Scheduled(cron = "0 0 0/1 * * ?")
    @Async
    public void doTrainViolationPoint() throws Exception{
        trainViolationPointService.doDS();
    }


}







