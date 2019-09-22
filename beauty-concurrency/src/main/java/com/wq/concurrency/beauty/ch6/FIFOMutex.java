package com.wq.concurrency.beauty.ch6;

import java.util.Queue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;

/**
 * 只有队首的线程能获取到锁并且unlock
 */
public class FIFOMutex {

    private final AtomicBoolean locked = new AtomicBoolean(false);
    private final Queue<Thread> waiters = new LinkedBlockingQueue<>();

    public static void main(String[] args) {
        FIFOMutex fifoMutex = new FIFOMutex();
        final int[] number = {0};
        for (int i = 0; i < 100; i++) {
            int finalI = i;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    /**
                     * 下面代码如果注释掉lock和unlock方法的话,就是线程不安全
                     */
                    try {
                        fifoMutex.lock(false);
                        number[0]++;
                        Thread.sleep(10);
                        System.out.println("i/number:" + finalI + "/" + number[0]);
                        fifoMutex.unlock();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            };
            Thread thread = new Thread(runnable);

            thread.start();
        }

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("done");
    }

    public void lock(boolean interrupt) throws InterruptedException {
        boolean currentWasInterrupted = false;
        Thread thread = Thread.currentThread();

        waiters.add(thread);

        while ((waiters.peek() != thread) || !locked.compareAndSet(false, true)) {
            LockSupport.park(this);
            if (Thread.interrupted()) {
                currentWasInterrupted = true;
            }
        }

        waiters.remove();
        if (currentWasInterrupted) {
            if (interrupt) {
                throw new InterruptedException();
            } else {
                //设置对应的中断状态,因为原本的中断状态在Thread.interrupted()方法中被清除了,但是通过currentWarInterrupted保留了下来
                thread.interrupt();
            }

        }

    }

    public void unlock() {
        locked.set(false);
        LockSupport.unpark(waiters.peek());
    }
}
