package com.wq.concurrency.beauty.ch5;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class CopyAndWriteListExample {

    public static void main(String[] args){
        CopyOnWriteArrayList<String> cow = new CopyOnWriteArrayList<>();

        cow.add("Hello");
        cow.add("World");
        cow.add("Jane");

        Thread thread = new Thread(()->{
           cow.set(1,"world");
           cow.add("love");
        });

        Iterator<String> iterator = cow.iterator();

        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //只会输出Hello World Jane,不会出现world和love
        //iterator返回的只是一个快照,而不是实时的,是弱一致性的体现
        while (iterator.hasNext()){
            System.out.println(iterator.next());
        }

        System.out.println("done");
    }
}
