package com.wq.concurrency.beauty.ch8;

import java.util.concurrent.SynchronousQueue;

public class SynchronizedQueueExample {

    public static void main(String[] args) {
        SynchronousQueue<String> queue = new SynchronousQueue<>();

        System.out.println("put a:" + queue.offer("a"));
        System.out.println("put b:" + queue.offer("b"));
    }
}
