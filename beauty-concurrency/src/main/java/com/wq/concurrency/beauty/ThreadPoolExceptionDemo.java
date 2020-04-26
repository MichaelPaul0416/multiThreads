package com.wq.concurrency.beauty;

import java.util.concurrent.*;

/**
 * @Author: wangqiang20995
 * @Date: 2020/2/25 16:26
 * @Description:
 **/
public class ThreadPoolExceptionDemo {

    public static void main(String[] args){
        ExecutorService executorService =
                new ThreadPoolExecutor(3,10,60, TimeUnit.SECONDS,new ArrayBlockingQueue<>(10));
        executorService.execute(()->{
            System.out.println("ok");
            throw new RuntimeException("error");
        });
    }
}
