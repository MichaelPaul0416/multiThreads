package com.wq.concurrency.jmm;

/**
 * @Author: wangqiang20995
 * @Date: 2019/12/7 16:44
 * @Description:
 **/
public class MemoryWatchableDemo {

    private static int x, y;

    private static void writer() {
        x = 1;
        y = 2;
    }

    private static void reader() {
        int a = x;
        int b = y;
        System.out.println("x|y > " + a + "|" + b);
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(()->{
            writer();
        });

        Thread t2 = new Thread(()->{
            reader();
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        }catch(InterruptedException e){
            e.printStackTrace();
        }

        System.out.println("done");
    }
}
