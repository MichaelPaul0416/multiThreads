package com.wq.concurrency.beauty.ch1;

/**
 * 讲述join方法和interrupt方法的搭配使用
 */
public class JoinAndInterrupt {

    public static void main(String[] args){

        Thread worker = new Thread(()->{
            //死循环
            for(;;){

            }
        });

        Thread main = Thread.currentThread();

        Thread waker = new Thread(()->{
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("wake up main thread...");
            main.interrupt();
        });

        worker.start();

        waker.start();

        try {
            worker.join();
        } catch (InterruptedException e) {
            System.out.println("main waken up...");
        }

        System.out.println("done");
    }
}
