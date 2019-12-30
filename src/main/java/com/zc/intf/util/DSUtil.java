package com.zc.intf.util;

import com.alibaba.fastjson.JSON;
import com.zc.intf.entity.BasicTrainMode;
import com.zc.intf.entity.BasicTrainTeam;
import com.zc.intf.entity.TrainNumberViolation;
import com.zc.intf.entity.TrainViolationFile;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DSUtil {

    /**
     * ------------------业务
     */

    public static String credential = "userName=zhusuoAPI&password=abcd@1234..";

    public static long lengthOfDay = 24 * 3600000;

    /**
     * 防止 空的JSON字段
     *
     * @param json
     * @param key
     * @return
     */
    public static String optString(JSONObject json, String key) {
        if (json.isNull(key)) {
            return null;
        } else {
            return json.optString(key, null);
        }
    }

    /**
     * 获取当前时间，年月日
     *
     * @return
     */
    public static String getNowTime() {
        SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdfTime.format(new Date());
    }

    /**
     * 获取当前时间减去一个小时
     *
     * @return
     */
    public static String getTimePlus(String time, int unit, int value) throws ParseException {
        //可以对每个时间域单独修改
        Calendar calen = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(time);
        calen.setTime(date);
        calen.add(unit, value);
        String timePlus = simpleDateFormat.format(calen.getTime());
        return timePlus;
    }

    /**
     * 获取当前时间减去一个小时
     *
     * @return
     */
    public static String getTimePlus(String time, long value) throws ParseException {
        //可以对每个时间域单独修改
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(time);
        date.setTime(date.getTime() + value);
        return simpleDateFormat.format(date);
    }

    /**
     * 比较两个时间是否超过
     *
     * @param time1
     * @param time2
     * @return
     * @throws ParseException
     */
    public static boolean timeDiffOverOneDay(String time1, String time2) throws ParseException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = simpleDateFormat.parse(time1);
        Date date2 = simpleDateFormat.parse(time2);

        return Math.abs(date1.getTime() - date2.getTime()) > lengthOfDay;

    }

    public static long diffTime(String startTime, String endTime) throws ParseException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = simpleDateFormat.parse(startTime);
        Date endDate = simpleDateFormat.parse(endTime);

        return endDate.getTime() - startDate.getTime();

    }

    public static int diffTimeDividedByDay(String startTime, String endTime) throws ParseException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = simpleDateFormat.parse(startTime);
        Date endDate = simpleDateFormat.parse(endTime);

        return (int) ((endDate.getTime() - startDate.getTime()) / lengthOfDay);

    }

    public static long remainderOfDividedByDay(String startTime, String endTime) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = simpleDateFormat.parse(startTime);
        Date endDate = simpleDateFormat.parse(endTime);

        return (endDate.getTime() - startDate.getTime()) % lengthOfDay;
    }


}
