package com.wq.concurrency.beauty.ch6;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class UnReentrantLock implements Lock {

    private final Sync sync = new Sync();

    @Override
    public void lock() {
        sync.acquire(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    @Override
    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1, unit.toNanos(time));
    }

    @Override
    public void unlock() {
        try {
            sync.release(1);
        } catch (Exception e) {
            //
            System.out.println("unlock exception:" + e.getLocalizedMessage());
        }
    }

    @Override
    public Condition newCondition() {
        return sync.newCondition();
    }

    private static class Sync extends AbstractQueuedSynchronizer {

        /**
         * 实现AQS的时候,需要重写的方法,表示是否是独占
         *
         * @return
         */
        @Override
        protected boolean isHeldExclusively() {
            return getState() == 1;
        }

        /**
         * 实现AQS的时候,需要重写的方法,尝试获取锁,AQS#acquire调用
         *
         * @param arg
         * @return
         */
        @Override
        protected boolean tryAcquire(int arg) {
            //由于是不可重入,所以arg只能=1,
            assert arg == 1;
            //由于是不可重入,所以state只能0->1
            if (compareAndSetState(0, 1)) {
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }

            return false;
        }

        @Override
        protected boolean tryRelease(int arg) {
            assert arg == 1;
            if (getState() == 0) {
                //state=0代表没人占用,此时还是释放的话就需要抛出异常
                throw new IllegalMonitorStateException();
            }

            //由于释放锁的时候,肯定是已经获取锁的线程,肯定是单线程,所以这里直接setState就可以了
            setExclusiveOwnerThread(null);
            setState(0);

            return true;
        }

        Condition newCondition() {
            return new ConditionObject();
        }
    }

    public static void main(String[] args) {
        UnReentrantLock lock = new UnReentrantLock();
        Condition notFull = lock.newCondition();
        Condition notEmpty = lock.newCondition();

        Queue<String> queue = new LinkedBlockingQueue<>();
        int size = 7;

        CountDownLatch produce = new CountDownLatch(10);

        CountDownLatch customer = new CountDownLatch(10);

        for (int i = 0; i < 10; i++) {
            int finalI = i;
            Thread thread = new Thread(() -> {
                String good = "good-" + finalI;
                lock.lock();
                try {
                    while (queue.size() == size) {
                        notEmpty.await();
                    }
                    System.out.println("["+Thread.currentThread().getName()+"] -- " + good);
                    queue.add(good);
                    Thread.sleep(10);
                    notFull.signal();
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(Thread.currentThread().getName() + "--" + e.getLocalizedMessage());
                } finally {
                    lock.unlock();
                    produce.countDown();
                }
            });
            thread.setName("Producer--" + i);
            thread.start();
        }

        for (int i = 0; i < 10; i++) {
            Thread consumer = new Thread(() -> {
                lock.lock();
                try {
                    while (queue.size() == 0) {
                        notFull.await();
                    }

                    String info = queue.poll();
                    System.out.println("Consumer[" + Thread.currentThread().getName() + "]:" + info);

                    Thread.sleep(15);

                    notEmpty.signal();
                } catch (Exception e) {
                    System.out.println(Thread.currentThread().getName() + "--" + e.getLocalizedMessage());
                } finally {
                    lock.unlock();
                    customer.countDown();
                }
            });
            consumer.setName("Consumer--" + i);
            consumer.start();
        }

        try {
            produce.await();
            customer.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("done");
    }
}
