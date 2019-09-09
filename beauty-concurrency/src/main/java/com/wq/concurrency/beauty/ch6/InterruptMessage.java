package com.wq.concurrency.beauty.ch6;

import java.util.concurrent.locks.LockSupport;

public class InterruptMessage {

    public static void main(String[] args){
        Thread thread = new Thread(()->{
            System.out.println("before park");

            //Thread.interrupted 方法返回当前线程的中断状态,并且清除中断状态
            while (!Thread.interrupted()){
                System.out.println("i am in");
                LockSupport.park();
            }

            System.out.println("after park");
        });
        thread.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("begin interrupt thread");

        thread.interrupt();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("done");
    }
}
