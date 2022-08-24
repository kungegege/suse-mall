package com.frame.task;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

//@Component
//@EnableScheduling
public class SchedulerTask {

    private static final SimpleDateFormat  f =new SimpleDateFormat("HH:mm:ss");


//    @Scheduled(fixedRate = 5000)
//    public void TimeTask(){
//        System.out.println(f.format(new Date()));
//    }

    // 每个5 秒执行一次
    @Scheduled(cron = "*/5 * * * * ?")
    public void TimeTask2(){
        System.out.println(f.format(new Date()));
    }
}
