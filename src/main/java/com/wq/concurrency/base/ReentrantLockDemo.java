package com.wq.concurrency.base;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @Author:wangqiang20995
 * @description:可重入锁
 * @Date:2017/9/24
 */
public class ReentrantLockDemo {
    public static void main(String args[]) throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        ReentrantAction action = new ReentrantAction(lock);

        Thread t1 = new Thread(new ReentrantTask(action,6));
        Thread t2 = new Thread(new ReentrantTask(action,4));
        t1.setName("ReentrantLock1");
        t2.setName("ReentrantLock2");

        t2.start();
        t1.start();

        Thread.sleep(1000);

        //日期转换功能
        System.out.println(LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE));
    }

}

class ReentrantTask implements Runnable{

    private ReentrantAction action;

    private int times;

    public ReentrantTask(ReentrantAction action,int times){
        this.action = action;
        this.times = times;
    }


    @Override
    public void run() {
        try {
            action.display(times);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class ReentrantAction{
    ReentrantLock lock;

    public ReentrantAction(ReentrantLock lock){
        this.lock = lock;
    }

    public void display(int times) throws InterruptedException {
        lock.lock();
        System.out.println(Thread.currentThread().getName() + "进入第一层lock");
        show(times);
        secondLock(times/2);
        lock.unlock();
        System.out.println(Thread.currentThread().getName() +"出第一层lock");
    }

    private void secondLock(int times) throws InterruptedException {
        lock.lock();
        System.out.println(Thread.currentThread().getName() + "进入第二层lock");
        for (int i = 0;i<times;i++){
            System.out.println(Thread.currentThread().getName() + "-->>>" + i);
            Thread.sleep(500);
        }
        lock.unlock();
        System.out.println(Thread.currentThread().getName() + "出第二层lock");
    }

    private void show(int times) throws InterruptedException {
        for (int i = 0;i<times;i++){
            System.out.println(Thread.currentThread().getName() + "-->" +i);
            Thread.sleep(1000);
        }
    }
}

class ReentrantLock{

    private int lockThread;//嵌套多层锁的时候，多层可重入锁嵌套使用

    private Thread currentThread;

    private boolean isLocked = false;

    public synchronized void lock() throws InterruptedException {

        Thread callThread = Thread.currentThread();
        while(isLocked && currentThread != callThread){
            wait();
        }

        isLocked = true;
        lockThread ++;
        currentThread = callThread;

    }

    public synchronized void unlock(){
        if(currentThread == Thread.currentThread()){
            lockThread --;
            if(lockThread == 0){
                isLocked = false;
                notify();
            }
        }
    }

}