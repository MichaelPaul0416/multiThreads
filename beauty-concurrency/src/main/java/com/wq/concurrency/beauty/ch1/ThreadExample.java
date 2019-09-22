package com.wq.concurrency.beauty.ch1;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class ThreadExample {
    /**
     * 当最后一个非守护线程结束的时候,JVM会正常推出,而不管当前是否还有守护线程
     */


    /**
     * 虚假唤醒:一个线程可以从挂起状态变为可运行状态
     * 即便该线程没有被其他线程调用notify(),notifyAll()方法进行通知,也没有被中断,或者等待超时
     * 处理:不停的去测试该线程被唤醒的条件是否满足,不满足的话那就让这个线程继续等待
     */

    private static class CallTask implements Callable<String>{

        @Override
        public String call() throws Exception {
            return "hello world";
        }
    }

    /**
     * 线程状态:创建--[执行start方法]-->就绪(获取了除CPU资源外的其他资源)--[获取了CPU资源]-->运行--[run方法执行完毕]-->终止
     */
    public static void main(String[] args){
        Callable<String> callable = new CallTask();
        //适配器,FutureTask本身是一个Runnable,内部适配了一个Callable
        FutureTask<String> futureTask = new FutureTask<>(callable);
        new Thread(futureTask).start();

        try {
            String result = futureTask.get();
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
