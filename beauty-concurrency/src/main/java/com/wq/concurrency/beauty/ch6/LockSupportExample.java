package com.wq.concurrency.beauty.ch6;

import java.util.concurrent.locks.LockSupport;

/**
 * 当线程执行LockSupport.park的时候,首先会判断当前线程是否与LockSupport进行关联,如果没有,则阻塞挂起当前线程,如果关联了,那么就立即返回[parkAndUnPark]方法
 * LockSupport.park与parkNanos方法,默认是不关联执行线程与LockSupport的许可证
 *
 */
public class LockSupportExample {

    public static void main(String[] args){
//        parkAndUnPark();
//        relationAndPark();
//        parkNanos();
        parkWithMonitor();
    }

    /**
     * JDK推荐使用LockSupport.park(Object)方法进行挂起当前线程,因为这个入参会被记录到线程内部
     * 好处是可以使用诊断工具观察线程被阻塞的原因,,打印线程堆栈排查问题的时候能知道是哪个类被阻塞了
     */
    private static void parkWithMonitor(){
        Thread block = new Thread(()->{
            System.out.println("before lock(this)");

            //一般入参是this,但是这里是静态方法,所以就不用this了
            LockSupport.park(new Object());

            /**
             * jstack 输出的信息
             * "Thread-0" #10 prio=5 os_prio=0 tid=0x00007f10102e3800 nid=0x1dff waiting on condition [0x00007f0fe1f03000]
             *    java.lang.Thread.State: WAITING (parking)
             * 	at sun.misc.Unsafe.park(Native Method)
             * 	- parking to wait for  <0x00000000d735fa60> (a java.lang.Object)
             * 	at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
             * 	at com.wq.concurrency.beauty.ch6.LockSupportExample.lambda$parkWithMonitor$0(LockSupportExample.java:28)
             * 	at com.wq.concurrency.beauty.ch6.LockSupportExample$$Lambda$1/1831932724.run(Unknown Source)
             * 	at java.lang.Thread.run(Thread.java:748)
             */

            System.out.println("after lock(this)");

        });

        block.start();

        try {
            block.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("never say it out....");

    }

    private static void parkNanos(){
        //1000ns之后返回
        LockSupport.parkNanos(1000);

        System.out.println("done");
    }


    private static void relationAndPark(){
        /**
         * 当一个线程调用unpark方法的时候,如果参数thread线程没有持有thread与LockSupport关联的许可证,那么就让参数thread持有
         * 如果thread之前因为调用park而被挂起,那么调用unpark之后就会被立刻唤醒
         * 如果thread之前没有调用park,那么调用unpark方法后,在调用park方法,那么就会被立刻返回
         */

        //当前线程没有持有许可证,所以关联许可证,让当前线程持有
        LockSupport.unpark(Thread.currentThread());

        System.out.println("before relation lock");

        LockSupport.park();

        System.out.println("after relation lock");

        System.out.println("done");
    }

    private static void parkAndUnPark(){
        Thread son  = new Thread(()->{
            System.out.println("before park son");

            LockSupport.park();

            System.out.println("after park son");
        });

        son.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        LockSupport.unpark(son);

        try {
            son.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("done");
    }
}
