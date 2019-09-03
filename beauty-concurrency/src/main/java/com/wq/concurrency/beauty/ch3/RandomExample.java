package com.wq.concurrency.beauty.ch3;

import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.*;

/**
 * Random在并发的情况下,生成随机数
 * 缺点:由于Random内部采用了AtomicLong,当并发量大的时候,会有大量的线程自旋竞争
 */
public class RandomExample {

    private static final ExecutorService executorService =
            new ThreadPoolExecutor(10, 200, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100));

    private static final ExecutorService executor =
            new ThreadPoolExecutor(10, 200, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100));

    public static void main(String[] args) {
        randomNumber();

        System.out.println("---------------------");

        threadLocalRandom();

    }

    /**
     * 使用ThreadLocalRandom并发生成随机数
     * 原理其实就是每个线程内部持有一个原始seed的副本,然后根据这个副本去生成一个新的seed,再将旧的seed更新为新的seed,最后根据新的seed生成随机数
     */
    public static void threadLocalRandom() {
        Map<ThreadLocalRandom, Integer> same = new ConcurrentHashMap<>();

        CountDownLatch count = new CountDownLatch(200);
        long start = 0;
        for (int i = 0; i < 200; i++) {
            if (i == 0) {
                start = System.currentTimeMillis();
            }
            executor.execute(() -> {
                /**
                 * 返回的是隶属于ThreadLocalRandom.class的一个成员属性
                 * 所以即便是多次调用,或者是并发调用,ThreadLocalRandom.current()返回的都是同一个对象
                 */
                ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
                same.put(threadLocalRandom, threadLocalRandom.hashCode());
                int r = threadLocalRandom.nextInt(100);
//                System.out.println("Thread[" + Thread.currentThread().getName() + "]-" + r);
                count.countDown();
            });
        }

        long end = 0;
        try {
            count.await();
            end = System.currentTimeMillis();
        } catch (InterruptedException e) {
            System.out.println("await result failed...");
        }

        //这行代码必然输出true
        System.out.println("same ThreadLocalRandom:" + (same.size() == 1));
        if (!executor.isShutdown()) {
            executor.shutdown();
        }
        System.out.println("ThreadLocalRandom done:" + (end - start));
    }

    /**
     * 使用Random并发生成随机数
     */
    private static void randomNumber() {
        //use default seed
        Random random = new Random();

        CountDownLatch countDownLatch = new CountDownLatch(200);
        long start = 0;
        for (int i = 0; i < 200; i++) {
            if (i == 0) {
                start = System.currentTimeMillis();
            }
            executorService.execute(() -> {
                int r = random.nextInt(100);
//                System.out.println("Thread[" + Thread.currentThread().getName() + "]-" + r);
                countDownLatch.countDown();
            });
        }

        long end = 0;
        try {
            countDownLatch.await();
            end = System.currentTimeMillis();
        } catch (InterruptedException e) {
            System.out.println("await result failed...");
        }

        if (!executorService.isShutdown()) {
            executorService.shutdown();
        }
        System.out.println("Random done:" + (end - start));
    }
}
