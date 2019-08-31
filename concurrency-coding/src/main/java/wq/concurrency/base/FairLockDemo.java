package wq.concurrency.base;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author:wangqiang20995
 * @description:公平锁的实现
 * @Date:2017/9/23
 */
public class FairLockDemo {
    public static void main(String args[]){
        FairLock fairLock = new FairLock();


        for(int i = 0;i<5;i++) {
            ActionDemo actionDemo = new ActionDemo(fairLock);
            Runnable fairTask = new FairTask(actionDemo, 2+i);
            Thread t = new Thread(fairTask);
            t.setName("Thread--" + (i+1));
            t.start();
        }
    }
}


class FairTask implements Runnable{

    private ActionDemo actionDemo;

    private int times;
    public FairTask(ActionDemo actionDemo,int times){
        this.actionDemo = actionDemo;
        this.times = times;
    }

    @Override
    public void run() {
        try {
            actionDemo.display(times);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class ActionDemo {
    private FairLock fairLock ;

    public ActionDemo(FairLock fairLock){
        this.fairLock = fairLock;
    }

    public void display(int times) throws InterruptedException {
        fairLock.lock();
        show(times);
        fairLock.unlock();
    }

    private void show(int times){

        for(int i =0;i<times;i++){
            System.out.println("【"+Thread.currentThread().getName() +"】"+ "HelloWorld-->["+i+"]");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class FairLock{
    private boolean isLock = false;//锁是否启用

    private List<QueueObject> waitThreads = new ArrayList<>();

    private Thread currentThread = null;

    public void lock() throws InterruptedException {
        QueueObject queueObject = new QueueObject();
        boolean isLockedForCurrentThread = true;
        synchronized (this){
            /**
             * 设置为同步的目的是防止多线程的环境下打乱插入顺序（一个线程插到一般，时间片用完了，被另一个线程插进去了，而且ArrayList也不是线程安全的）
             */
            waitThreads.add(queueObject);
        }

        while(isLockedForCurrentThread){
            synchronized (this){
                isLockedForCurrentThread = isLock || waitThreads.get(0) != queueObject;
                if(!isLockedForCurrentThread){
                    waitThreads.remove(queueObject);
                    isLock = true;
                    currentThread = Thread.currentThread();
                    return;
                }
            }

            /**
             *凡是执行到这里的，说明队列中已经有等待的线程【其实就是那个线程拥有的对象监视器】
             * 由于对象监视器是每一个线程调用lock方法时新生成的，所以每个对象监视器和该线程其实是绑定的，所以只要调绑定的对象监视器的wait方法就可以了
             */
            try {
                queueObject.doWait();
            } catch (InterruptedException e) {
                /**
                 * 等待线程被假唤醒的话，也需要从队列中移除该线程
                 */
                synchronized (this){
                    waitThreads.remove(queueObject);
                }
                throw e;
            }
        }
    }

    public synchronized void unlock(){
        if(currentThread != Thread.currentThread()){
            throw new IllegalMonitorStateException(Thread.currentThread().getName() + "不是当前获取对象监视器的线程");
        }
        isLock = false;
        currentThread = null;
        if(waitThreads.size() > 0) {
            waitThreads.get(0).doNotify();
        }
    }
}

class QueueObject{
    private boolean isNotify = false;

    public synchronized void doWait() throws InterruptedException {
        while(!isNotify){
            //自旋锁，被从等待队列中移除唤醒之前，一直让其处于wait状态
            wait();
        }

        //从等待队列中移除之后，将其置为false，等待下次执行
        isNotify = false;
    }

    public synchronized void doNotify(){
        isNotify = true;

        //被其他线程调用，将自身唤醒
        this.notify();
    }

    public boolean equals(Object object){
        return this == object;
    }
}

