package com.wq.concurrency.beauty.ch1;

import java.util.concurrent.TimeUnit;

/**
 * 关于释放锁的demo
 */
public class ReleaseSyncLock {

    public static void main(String[] args){
        Object monitorA = new Object();
        Object monitorB = new Object();

        /**
         * 下面的两个runnable一起执行的话,会发生死锁,Object.wait方法只会释放自己这把锁,不会释放别的object的锁
         */
        Runnable r1 = new Runnable() {
            @Override
            public void run() {
                synchronized (monitorA){
                    System.out.println("Thread["+Thread.currentThread().getName()+"] get lockA");
                    System.out.println("Thread["+Thread.currentThread().getName()+"] try to get LockB");
                    synchronized (monitorB){
                        System.out.println("Thread["+Thread.currentThread().getName()+"] get lockB");
                        System.out.println("Thread["+Thread.currentThread().getName()+"] release LockA");
                        try {
                            monitorA.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };

        Runnable r2 = new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    System.out.println("sleep interrupted...");
                }

                synchronized (monitorA){
                    System.out.println("Thread["+Thread.currentThread().getName()+"] get lockA");
                    System.out.println("Thread["+Thread.currentThread().getName()+"] try to get LockB");
                    synchronized (monitorB){
                        System.out.println("Thread["+Thread.currentThread().getName()+"] get lockB");
                        System.out.println("Thread["+Thread.currentThread().getName()+"] release LockA");
                        try {
                            monitorA.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };

        Thread t1 = new Thread(r1);
        t1.setName("Thread-A");
        Thread t2 = new Thread(r2);
        t2.setName("Thread-B");

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        }catch (InterruptedException e) {
            System.out.println("task interrupted...");
        }

        System.out.println("done");
    }
}
