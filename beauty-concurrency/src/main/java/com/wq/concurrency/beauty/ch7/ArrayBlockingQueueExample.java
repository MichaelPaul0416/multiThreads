package com.wq.concurrency.beauty.ch7;

import java.util.concurrent.ArrayBlockingQueue;

public class ArrayBlockingQueueExample {

    public static void main(String[] args){
        ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<>(10);
        queue.offer("hello");
        try {
            /**
             * 阻塞方法,阻塞直到放进去,或者线程被中断,抛出中断异常
             */
            queue.put("world");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String head = queue.poll();
        System.out.println("queue head:" + head);

        try {
            /**
             * 阻塞方法,阻塞直到队列中有元素
             */
            queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
