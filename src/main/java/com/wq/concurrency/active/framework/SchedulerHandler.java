package com.wq.concurrency.active.framework;

import java.io.Serializable;

/**
 * @Author: wangqiang20995
 * @Date:2019/1/19
 * @Description:
 * @Resource:
 */
public class SchedulerHandler extends Thread {

    private SimpleSaveQueue simpleSaveQueue ;

    public SchedulerHandler(int length){
        simpleSaveQueue = new SimpleSaveQueue(length);
    }


    public <T extends Serializable> boolean invoke(MethodRequest<T> methodRequest){
        try {
            this.simpleSaveQueue.set(methodRequest);
            return true;
        } catch (InterruptedException e) {
            System.out.println("请求加入安全队列失败");
            return false;
        }
    }

    @Override
    public void run() {

        while (true){
            try {
                MethodRequest<Serializable> methodRequest = simpleSaveQueue.get();
                methodRequest.execute();

            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + "-->执行出现异常");
            }
        }
    }
}
