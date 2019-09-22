package com.wq.concurrency.beauty.ch6;

public class ThreadExample {

    private static boolean flag;

    static{

        Thread main = Thread.currentThread();
        Thread thread = new Thread(()->{
//            flag = true;
//            int i = 0;
//            i++;
//            System.out.println(Thread.currentThread().getContextClassLoader().getClass().getName());
//            System.out.println("12");
            main.interrupt();
        });
        thread.start();
        System.out.println(Thread.currentThread().getContextClassLoader().getClass().getName());
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        System.out.println("123");
        System.out.println(flag);
    }


}
