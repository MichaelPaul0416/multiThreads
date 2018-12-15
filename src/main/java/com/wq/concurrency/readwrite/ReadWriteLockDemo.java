package com.wq.concurrency.readwrite;


import java.util.concurrent.*;

/**
 * @Author: wangqiang20995
 * @Date:2018/12/15
 * @Description:
 * @Resource:
 */
public class ReadWriteLockDemo {

    public static void main(String args[]) {

        DataResource dataResource = new DataResource(100);

        ExecutorService writeService = new ThreadPoolExecutor(10,10,0L,TimeUnit.SECONDS,new ArrayBlockingQueue<>(100));
        for(int i = 0;i<2;i++){
            writeService.execute(new Writer(dataResource,"HelloWorld" + (i+1)));
        }


        ExecutorService readerService = new ThreadPoolExecutor(10,10,0L,TimeUnit.SECONDS,new ArrayBlockingQueue<>(100));
        for(int i = 0;i<100;i++){
            readerService.execute(new Reader(dataResource));
        }



        writeService.shutdown();
        readerService.shutdown();
    }

    public static class Reader implements Runnable {

        private DataResource dataResource;

        public Reader(DataResource dataResource) {
            this.dataResource = dataResource;
        }

        @Override
        public void run() {
            char[] message;
            try {
                message = this.dataResource.readMessage();
                System.out.println(String.format("[%s]--reader->[%s]", Thread.currentThread().getName(), String.valueOf(message)));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public static class Writer implements Runnable {

        private DataResource dataResource;
        private String message;

        public Writer(DataResource dataResource, String message) {
            this.dataResource = dataResource;
            this.message = message;
        }

        @Override
        public void run() {
            try {
                if(this.dataResource.writeMessage(message)){
                    System.out.println(String.format("[%s]--writer->[%s]",Thread.currentThread().getName(),message));
                }else {
                    System.out.println(String.format("[%s] write message[%s] failed",Thread.currentThread().getName(),message));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
