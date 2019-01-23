package com.wq.concurrency.active.client;

import com.wq.concurrency.active.framework.core.SchedulerHandler;

/**
 * @Author: wangqiang20995
 * @Date:2019/1/23
 * @Description:
 * @Resource:
 */
public class ClientDemoTest {

    public static void main(String args[]) {

        SchedulerHandler schedulerHandler = new SchedulerHandler(100);


        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(new ClientDateTimeWorker("DateTimer-" + (i + 1),
                    ServiceCommonProxy.instanceProxy(schedulerHandler)));
            thread.setName("DateTimer-" + (i + 1));
            thread.start();
        }

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < 5; i++) {
            Thread thread = new Thread(new ClientLogWorker("SaveLogger-" + (i + 1),
                    ServiceCommonProxy.instanceProxy(schedulerHandler)));
            thread.setName("SaveLogger-" + (i + 1));
            thread.start();
        }

        try {
            Thread.sleep(20 * 1000);
        } catch (InterruptedException e) {
            System.out.println("等待所有线程执行完毕异常");
        }

        System.out.println("提交关闭请求...");


        schedulerHandler.stopHandler();


        try {
            schedulerHandler.join();
        } catch (InterruptedException e) {
            System.out.println("等待Scheduler线程结束异常");
        }


        System.out.println("all done");
    }
}
