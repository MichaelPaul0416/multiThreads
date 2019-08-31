package wq.concurrency.base;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author:wangqiang20995
 * @description:非公平锁的定义
 * @Date:2017/9/23
 */
public class SimpleLockDemo {
    public static void main(String args[]){

        Lock lock = new Lock();
        action action = new action(lock);

        Thread t1 = new Thread(new lockTest(action,3));
        Thread t2 = new Thread(new lockTest(action,5));

        t1.setName("worker");
        t2.setName("manager");

        t1.start();
        t2.start();
    }
}

class lockTest implements Runnable{

    private action action;

    private int times ;

    public lockTest(action action,int times){
        this.action = action;
        this.times = times;
    }

    @Override
    public void run() {
        try {
            action.display(this.times);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class action{
    private Lock lock;


    public action(Lock lock){
        this.lock = lock;
    }

    public void display(int times) throws InterruptedException {
        lock.lock();
        show(times);
        lock.unlock();
    }

    private void show(int times){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        for(int i = 0;i<times;i++){
            Date date = new Date();
            System.out.println(Thread.currentThread().getName() + "[" + simpleDateFormat.format(date) + "]");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Lock{
    private boolean isLocked = false;

    private Thread current = null;

    public void lock() throws InterruptedException {
        synchronized (this){
            /**
             * 下面这个自旋锁的意义在于第一次进来如果对象监视器已经被获取了，那么就阻塞在这里
             * 只有两个线程的话换成if也可以，但是不会一直循环监听了，信号量可能会错过
             */
            while(isLocked){
                wait();
            }

            isLocked = true;
            current = Thread.currentThread();
        }
    }

    public void unlock(){
        synchronized (this) {
            if (current != Thread.currentThread()) {
                throw new RuntimeException(Thread.currentThread().getName() + "不是当前获取对象监视器的线程");
            }

            isLocked = false;

            current = null;

            notify();
        }
    }
}