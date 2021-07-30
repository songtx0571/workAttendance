package com.howei.util;

import com.howei.pojo.ManagerHours;
import com.howei.service.WorkingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ScheduledTask {

    Logger logger = LoggerFactory.getLogger(ScheduledTask.class);
    @Autowired
    private WorkingService workingService;

    @Scheduled(cron = "30 59 23 * * ?")
    public void scheduledTaskByCorn() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("monthDay", sdf.format(date));
        paramsMap.put("type", 0);
        List<ManagerHours> managerHoursList = workingService.getManagerHoursListByMap(paramsMap);
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (managerHoursList != null && managerHoursList.size() > 0) {
            for (ManagerHours managerHours : managerHoursList) {
                try {
                    managerHours.setType(1);
                    managerHours.setWorkEndTime(sdf.format(date));
                    String workStartTime = managerHours.getWorkStartTime();
                    Date parseWorkStartTime = sdf.parse(workStartTime);
                    double workingHour = (date.getTime() - parseWorkStartTime.getTime()) / 3600 / 1000D;
                    BigDecimal bigDecimal = new BigDecimal(workingHour).setScale(2, BigDecimal.ROUND_HALF_UP);
                    managerHours.setWorkingHour(bigDecimal.doubleValue());
                    workingService.updateManagerHours(managerHours);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        logger.info("时间为" + sdf.format(date) + ",所有管理着已经设置下班打卡");

    }


}