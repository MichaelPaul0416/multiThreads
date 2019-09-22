package com.wq.concurrency.beauty.ch4;

import java.util.concurrent.atomic.AtomicLong;

/**
 * AtomicLong的学习使用
 */
public class AtomicLongExample {

    public static final int[] arrayOne = new int[]{0,2,3,0,1,6,10,8,0,4};

    public static final int[] arrayTwo = new int[]{10,1,2,3,0,5,6,0,7,0,8};

    private static final AtomicLong zero = new AtomicLong(0);

    public static void main(String[] args){
        Thread one = new Thread(()->{
            for(int item:arrayOne){
                if(item == 0){
                    zero.getAndIncrement();
                }
            }
        });

        Thread two = new Thread(()->{
            for(int item : arrayTwo){
                if (item == 0){
                    zero.getAndIncrement();
                }
            }
        });

        one.start();
        two.start();

        try {
            one.join();
            two.join();
        }catch (InterruptedException e){
            System.out.println("wait for result error:" + e.getMessage());
        }

        System.out.println("count 0:" + zero.get());
    }
}
