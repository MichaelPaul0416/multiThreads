package com.wq.concurrency.beauty.ch2;

public class VolatileDemo {
    //没有用volatile修饰,这个变量不是内存可见的,所以下面的输出结果可能是0,而不是4
    private static int num = 0;
    private static boolean ready = false;

    private static class ReaderThread extends Thread{
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()){
                if(ready){// code-1
                    System.out.println(num + num);// code-2
                }
                System.out.println("read thread...");
            }
        }
    }

    private static class WriterThread extends Thread{
        @Override
        public void run() {
            /**
             * code-3 code-4之间没有必然的联系,所以可能存在指令重排序,也就是先执行了code-4,然后执行code-3,但是还没来得及执行code-3的时候
             * cpu的时间片到了,交给reader线程执行,然后reader判断ready=true,对num进行叠加,此时运算的输出就是0而不是4
             */
            num = 2;// code-3
            ready = true;// code-4
            System.out.println("write thread set over...");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ReaderThread readerThread = new ReaderThread();
        readerThread.start();

        WriterThread writerThread = new WriterThread();
        writerThread.start();

        Thread.sleep(10);

        readerThread.interrupt();
        System.out.println("main exit");
    }
}
