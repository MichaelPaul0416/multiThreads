package com.wq.concurrency.beauty.ch8;

import java.util.concurrent.TimeUnit;

public class ThreadExitExample {

    private static final int LENGTH = 10;

    /**
     * 当一个线程执行System.exit的时候，其他线程都会退出，然后进程结束
     * @param args
     */
    public static void main(String[] args) {
        // 先创建10个线程
        Thread[] workers = new Thread[LENGTH];
        for (int i = 0; i < LENGTH; i++) {
            int finalI = i;
            Thread thread = new Thread(() -> {
                System.out.println("thread name [worker-" + finalI + "]");
                // 某一个线程执行系统退出
                if (finalI == 7) {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        System.out.println("error:" + e.getLocalizedMessage());
                    }
                    System.exit(-1);
                } else {
                    while (true) {
                    }
                }
            });
            workers[i] = thread;
        }

        // start
        for (int i = 0; i < workers.length; i++) {
            workers[i].start();
        }

        // main thread wait forever
        while (true){}
    }
}
