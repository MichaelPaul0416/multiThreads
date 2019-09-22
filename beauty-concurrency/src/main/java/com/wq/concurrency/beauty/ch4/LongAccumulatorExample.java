package com.wq.concurrency.beauty.ch4;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.LongAccumulator;

public class LongAccumulatorExample {

    public static void main(String[] args) {
        //第一个参数指定运算的规则,其实就是一个Function,第二个指定一个初始值
        //该类相比于LongAddr,不同的是可以1.指定初始化的值为identity,指定迭代规则Function
        //第一个参数left是当前cell的base值,第二个参数right是方法accumulate传入的值
        LongAccumulator accumulator = new LongAccumulator((left, right) -> {
            System.out.println(Thread.currentThread().getName() + ">>" + left + "|" + right);
            return left + right + 2;
        }, 1);

        CountDownLatch countDownLatch = new CountDownLatch(10);

        for (int i = 0; i < 10; i++) {
            int finalI = i;
            new Thread(() -> {
                accumulator.accumulate(finalI);
                countDownLatch.countDown();
            }).start();
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //最终结果=accumulate方法的入参累加[45]+function中的left+right+2中的2 * 次数[10] + identity[1]=0+1+2+3+...+9+2*10+1
        System.out.println("finally:" + accumulator.get());
    }
}
