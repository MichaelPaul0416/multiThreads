package com.wq.concurrency.beauty.ch9;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SemaphoreExample {

    /**
     * Semaphore一开始并不关心线程个数,通过release方法递增信号量的个数,然后通过acquire来一次性获取信号量
     * 如果信号量的个数不够acquire的方法入参指定的个数的话,那么调用acquire方法的线程就会阻塞,直到信号量个数满足为止
     * @param args
     */
    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(0);

        ExecutorService executorService = Executors.newFixedThreadPool(5);

        for (int i = 0; i < 5; i++) {
            executorService.execute(()->{
                System.out.println(Thread.currentThread().getName()+" - release semaphore and come in");
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    System.out.println(e.getLocalizedMessage());
                }
                semaphore.release();
                System.out.println(Thread.currentThread().getName()+" - come out");
            });
        }

        try {
            /**
             * 希望获取一个或者n个信号量资源,成功后信号量计数会减去入参
             * 如果信号量个数=0,那么当前线程就会被放入aqs的阻塞队列中
             */
            semaphore.acquire(5);
            System.out.println("semaphore all get and return it...");
        } catch (InterruptedException e) {
            System.out.println(e.getLocalizedMessage());
        }

        executorService.shutdown();
    }
}
