package com.wq.concurrency.beauty.ch1;

/**
 * 线程中断
 * 实例方法
 * interrupt():设置线程实例A的中断标志为true,并且立即返回,线程A会继续往下执行,如果线程A因为调用了wait,join,sleep方法导致被阻塞挂起,那么这个时候就会抛出InterruptedException异常
 * isInterrupted:检测当前线程是否被中断,如果是返回true,否则返回false,它不会清除中断标识
 *
 * 静态方法
 * boolean interrupted():检查当前线程是否被终端,如果是返回true,否则返回false;
 * 与实例方法isInterrupted()不同的是,该方法如果发现当前线程被中断,就会清除中断标识
 */
public class InterruptThread {
    public static void main(String[] args){
        Thread thread = new Thread(()->{
            while (!Thread.currentThread().isInterrupted()){
                System.out.println("Thread["+Thread.currentThread().getName()+"]-->Hello");
            }

            System.out.println("worker is interrupted...");
        });
        thread.setName("worker");
        thread.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("waken up while sleeping");
        }

        //
        System.out.println("main will interrupt worker...");
        thread.interrupt();

        try {
            thread.join();
        } catch (InterruptedException e) {
            System.out.println("wait for result but interrupted...");
        }

        System.out.println("done");
    }
}
