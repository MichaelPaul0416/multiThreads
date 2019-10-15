package com.wq.concurrency.beauty.ch9;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: wangqiang20995
 * @Date:2019/10/14
 * @Description:
 * @Resource:
 */
public class ScheduleThreadPoolExample {

    public static void main(String[] args) {
        ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(10);

        System.out.println("start[" + time() + "]");
        // 设置一次性任务
//        scheduledExecutorService.schedule(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("[once]一次性任务");
//            }
//        },1, TimeUnit.SECONDS);

        // 设置固定延迟时间的任务，但是任务不超时
//        scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("[fixedDelay][" + time() + "]程序启动后2s执行，往后每5s执行一次");
//            }
//        }, 2, 5, TimeUnit.SECONDS);

        // 设置固定延迟时间的任务，但是任务会超时
        // 下一次执行任务的时间=当前任务开始执行的时间+任务执行需要的时间+固定延迟
//        scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("[fixedDelay]["+time()+"]超时任务，，，程序1s后执行，往后每3s执行一次，但是任务执行周期是5s");
//                try {
//                    TimeUnit.SECONDS.sleep(5);
//                } catch (InterruptedException e) {
//                    System.out.println(e.getMessage());
//                }
//            }
//        },1,3,TimeUnit.SECONDS);

        // 设置固定频率的任务
//        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("[fixedRate][" + time() + "]程序启动后3s执行，然后按照固定频率执行");
//            }
//        }, 3, 2, TimeUnit.SECONDS);

        // 设置固定频率的任务，但是任务会超时
        // 下次执行的时间=当前执行的时刻 + (任务执行时间是否超过频率间隔时间 ? 任务执行需要的时间 : 频率间隔的时间)
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("[fixedRate][" + time() + "]程序启动后3s执行，往后按照每2s一次的频率执行，但是任务会超时");
                try {
                    TimeUnit.SECONDS.sleep(4);
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }
        },3,2,TimeUnit.SECONDS);

    }

    private static String time() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(new Date());
    }
}
