package com.wq.concurrency.beauty.ch7;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @Author: wangqiang20995
 * @Date:2019/9/22
 * @Description:
 * @Resource:
 */
public class ConcurrentLinkedQueueExample {

    public static void main(String[] args){

        ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();
        queue.offer("hello");
        queue.offer("world");

        String head = queue.peek();
        System.out.println(head);
        System.out.println(queue.poll());
    }
}
