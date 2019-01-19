package com.wq.concurrency.active.framework;

import java.io.Serializable;

/**
 * @Author: wangqiang20995
 * @Date:2019/1/19
 * @Description:
 * @Resource:
 */
public class SimpleSaveQueue<T extends Serializable> {

    private volatile MethodRequest<T>[] methodRequests;

    private int number;

    private int head = 0;

    private int tail = 0;


    private final Object monitor = new Object();

    public SimpleSaveQueue(int number) {
        methodRequests = new MethodRequest[number];
    }

    public void set(MethodRequest<T> methodRequest){

        synchronized (monitor){
            if(this.he)
        }
    }
}
