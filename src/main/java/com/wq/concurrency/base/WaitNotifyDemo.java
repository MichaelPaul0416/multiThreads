package com.wq.concurrency.base;

/**
 * @Author:wangqiang20995
 * @description:
 * @Date:2017/9/23
 */
public class WaitNotifyDemo {
    public static void main(String args[]) throws InterruptedException {
        advice advice = new advice();
        Thread t1 = new Thread(new worker(advice));
        Thread t2 = new Thread(new manager(advice));

        t1.setName("worker");
        t2.setName("manager");

        t1.start();
        t2.start();

        if(Thread.currentThread().getName().equals("manager")){
            Thread.sleep(1000);
        }

    }
}

class advice{
    /**
     * 对象监视器不能是字符串或者final常量，因为他们被保存在常量池中，以下代码中a,b分别是advice的两个实例，但是输出为true
     * System.out.println(a.sign == b.sign);
     * 这就意味这如果把敞亮作为对象监视器的话，那么即便是不同的实例对象，甚至是不同的类的实例对象，只要用了同一个常量，那么就会一起竞争资源
     * */
    Object lock = new Object();

    boolean sign = false;
    public void doWait(){
        System.out.println(Thread.currentThread().getName() + "即将进入等待，释放监视对象器上的锁");
        synchronized(lock){
            while(!sign) {//自旋锁
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(Thread.currentThread().getName() + "被唤醒了，更改信号量");
            sign = false;
        }
    }

    public void doNotify(){
        System.out.println(Thread.currentThread().getName() + "即将唤醒监视对象器上的等待线程");
        synchronized (lock){
            sign = true;
            lock.notify();
        }
    }

}


class worker implements Runnable{

    advice advice;

    public worker(advice advice){
        this.advice = advice;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + "很累了，需要休息一会");
        advice.doWait();
        System.out.println(Thread.currentThread().getName() + "休息好了，准备开始工作了");
    }
}

class manager implements Runnable{
    advice advice;

    public manager(advice advice){
        this.advice = advice;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + "工作线程已经休息够久了，准备叫醒他们开始工作了");
        advice.doNotify();
//        System.out.println(Thread.currentThread().getName() + "工作线程工作时间有点长了噢");
    }

}

