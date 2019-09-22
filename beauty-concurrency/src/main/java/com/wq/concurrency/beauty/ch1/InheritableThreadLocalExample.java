package com.wq.concurrency.beauty.ch1;

/**
 * 子线程获取父线程设置的threadLocal
 */
public class InheritableThreadLocalExample {

    public static void main(String[] args){
        ThreadLocal<String> container = new InheritableThreadLocal<>();
        container.set("i am your father");
        Thread son = new Thread(()->{

            System.out.println("lang:" + container.get());
        });

        son.start();

        try {
            son.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("done");
    }
}
