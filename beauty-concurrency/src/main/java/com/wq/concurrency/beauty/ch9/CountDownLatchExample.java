package com.wq.concurrency.beauty.ch9;

import java.util.concurrent.*;

public class CountDownLatchExample {

    public static void main(String[] args){
        /**
         * CountDownLatch与Thread#join方法的区别
         * 父线程调用子线程的Thread.join方法之后,父线程需要等待子线程执行完毕后,才能继续执行
         * 但是CountDownLatch#countDown可以在子线程的任何地方调用,当CountDownLatch的计数=0的时候,父线程就可以继续往下执行
         */
        CountDownLatch countDownLatch = new CountDownLatch(3);

        ExecutorService executorService = new ThreadPoolExecutor(5,10,10, TimeUnit.MINUTES,new ArrayBlockingQueue<>(10));

        for(int i = 0;i<3;i++){
            int finalI = i;
            executorService.execute(()->{
                System.out.println("number:" + finalI);
                countDownLatch.countDown();
            });
        }

        // 可以有多个线程进行等待,等待计数器减为0,这些等待的线程,全在AQS的计数器上
        executorService.execute(()->{
            System.out.println("子线程等待:countDownLatch#await");
            try {
                countDownLatch.await();
                System.out.println("子线程等待done");
            } catch (InterruptedException e) {
                System.out.println(e.getLocalizedMessage());
            }
        });

        try {
            countDownLatch.await();
            // 这里再调用一次await方法,对于执行的结果是没有影响的,也就是说,当前计数器如果=0,那么await方法是不会阻塞的
            countDownLatch.await();
        } catch (InterruptedException e) {
            System.out.println(e.getLocalizedMessage());
        }

        // 此时计数器=0,再调用的话,countDown/await方法都会立即返回
        countDownLatch.countDown();
        executorService.shutdown();
    }
}
