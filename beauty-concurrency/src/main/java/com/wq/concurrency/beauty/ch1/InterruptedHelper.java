package com.wq.concurrency.beauty.ch1;

import java.util.concurrent.TimeUnit;

/**
 * 使用中断的方式唤醒线程
 */
public class InterruptedHelper {

    public static void main(String[] args){
        Object monitor = new Object();
        Thread thread = new Thread(()->{
            synchronized (monitor){
                System.out.println("get lock");
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    System.out.println("wake up thread...");
                }
            }
        }) ;

        thread.start();

        try {
            TimeUnit.SECONDS.sleep(3);
            System.out.println("main interrupt thread ...");
            thread.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            thread.join();
        } catch (InterruptedException e) {
            System.out.println("wait for result but interrupted...");
        }

        System.out.println("done");
    }
}
