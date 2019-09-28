package com.wq.concurrency.beauty.ch7;

import java.util.Random;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @Author: wangqiang20995
 * @Date:2019/9/28
 * @Description:
 * @Resource:
 */
public class DelayQueueExample {

    static class DelayedEle implements Delayed{

        private final long delayTime;
        private final long expire;

        private String taskName;

        public DelayedEle(long delayTime,String taskName){
            this.delayTime = delayTime;
            this.taskName = taskName;
            this.expire = System.currentTimeMillis() + this.delayTime;
        }

        /**
         * 获取剩余时间
         * @param unit
         * @return
         */
        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(this.expire - System.currentTimeMillis(),TimeUnit.MILLISECONDS);
        }

        @Override
        public int compareTo(Delayed o) {
            return (int)(this.getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS));
        }

        @Override
        public String toString() {
            return "DelayedEle{" +
                    "delayTime=" + delayTime +
                    ", expire=" + expire +
                    ", taskName='" + taskName + '\'' +
                    '}';
        }
    }
    public static void main(String[] args){
        DelayQueue<DelayedEle> delayedEles = new DelayQueue<>();

        Random random = new Random();
        for(int i = 0;i<10;++i){
            DelayedEle ele = new DelayedEle(random.nextInt(500),"task:" + i);
            delayedEles.offer(ele);
        }

        DelayedEle delayedEle;
        try{
            int count = 0;
            int size = delayedEles.size();
            for(;;){
                while (count < size && (delayedEle = delayedEles.take()) != null){
                    System.out.println(delayedEle);
                    count ++;
                }
                break;
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
