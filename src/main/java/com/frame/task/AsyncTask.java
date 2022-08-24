package com.frame.task;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;


//@Component
public class AsyncTask {

    @Async
    public void AsyncTask1(){
        System.out.println("AsyncTask1 - 任务完成");
    }

    @Async
    public void AsyncTask2(){
        System.out.println("AsyncTask2 - 任务完成");
    }

    /*带有回调的异步任务*/
    @Async
    public Future<String> AsyncTask3(){
        return new AsyncResult<>("AsyncTask3 - 任务完成");
    }

}
