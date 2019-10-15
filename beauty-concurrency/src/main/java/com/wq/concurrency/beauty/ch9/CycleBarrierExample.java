package com.wq.concurrency.beauty.ch9;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CycleBarrierExample {
    /**
     * 第一个参数表示多少线程调用CyclicBarrier#await方法之后.之前阻塞的这些线程会冲破屏障,继续往下执行
     * 第二个参数就是当最后一个线程使计数器=0的时候,那个线程先来执行这个run方法,然后执行自己的方法
     * 冲破屏障后,会将第一个参数复制给计数器,此时后面又可以进行下一轮的屏障
     */
    private static CyclicBarrier cyclicBarrier = new CyclicBarrier(3, () -> {
        System.out.println("[" + Thread.currentThread().getName() + "]start to merge all result parts...");
    });

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 3; i++) {
            executorService.execute(() -> {
                System.out.println(Thread.currentThread().getName() + " enter...");
                try {
                    /**
                     * 如果屏障的总值=N,那么每一个线程执行一次CyclicBarrier#await的时候,屏障值就会-1
                     * 然后等第N个线程执行完了之后,屏障值=0,此时第N个线程先执行构造器中传入的Runnable#run方法
                     * 然后唤醒前面的N-1个线程,让他们各自去执行CyclicBarrier#await方法之后的代码
                     * 也就是说,屏障值=0的时候,第N个线程先执行构造器的Runnable#run,然后执行await方法之后的代码
                     * 然后剩余N-1个线程并发抢占执行
                     */
                    /**
                     * 从await方法返回的条件
                     * 1.有构造器中第一个参数parties个线程调用了await方法,此时计数器=0
                     * 2.其他线程中断了当前线程,抛出InterruptedException
                     * 3.当前屏障点关联的Generation对象的broken标志被改为true,抛出BrokenBarrierException
                     */
                    cyclicBarrier.await();
                } catch (Exception e) {
                    System.out.println(e.getLocalizedMessage());
                } finally {
                    System.out.println(Thread.currentThread().getName() + " out...");
                }
            });
        }

        executorService.shutdown();
    }
}
