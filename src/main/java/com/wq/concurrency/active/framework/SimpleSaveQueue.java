package com.wq.concurrency.active.framework;

import java.io.Serializable;

/**
 * @Author: wangqiang20995
 * @Date:2019/1/19
 * @Description:简单的FIFO线程安全队列,泛型类型为具体的返回值
 * @Resource:
 */
public class SimpleSaveQueue<T extends Serializable> {

    private volatile MethodRequest<T>[] methodRequests;

    private int head = 0;//从队列中取元素的下标

    private int tail = 0;//将元素放入队列的下标

    private int count;//取值范围是[1,methodRequests.length]


    private final Object monitor = new Object();

    public SimpleSaveQueue(int number) {
        methodRequests = new MethodRequest[number];
    }

    //synchronized 锁住的对象监视器必须和调用wait()/notify()/notifyAll()方法的对象是同一个，不然会抛出IllegalMonitorStateException
    public void set(MethodRequest<T> methodRequest) throws InterruptedException {

        synchronized (monitor){
            while (count >= methodRequests.length){
                monitor.wait();
            }

            methodRequests[tail] = methodRequest;
            tail = (tail + 1) % methodRequests.length;
            count ++;
            monitor.notifyAll();
        }
    }

    public MethodRequest<T> get() throws InterruptedException {
        synchronized (monitor){
//            System.out.println("["+Thread.currentThread().getName()+"] count left --> " + count);
            while (count <= 0){
                monitor.wait();
            }

            MethodRequest<T> requests = methodRequests[head];
            head = (head + 1) % methodRequests.length;
            count --;
            monitor.notifyAll();

            return requests;
        }
    }

    public boolean empty(){
        return this.count == 0;
    }
}
