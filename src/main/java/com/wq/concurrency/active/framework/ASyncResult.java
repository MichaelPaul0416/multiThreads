package com.wq.concurrency.active.framework;

import java.io.Serializable;

/**
 * @Author: wangqiang20995
 * @Date:2019/1/19
 * @Description:异步结果接收对象
 * @Resource:
 */
public class ASyncResult<T extends Serializable> extends Result<T> implements Serializable{

    private SyncResult<T> syncResult;

    private volatile boolean done = false;

    private Object monitor = new Object();

    @Override
    public T get() {
        synchronized (monitor) {
            while (!done) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    System.out.println(Thread.currentThread().getName() + "休眠状态被打断，检查结果是否已经准备好了");
                }
            }

            return syncResult.get();
        }
    }

    @Override
    public void set(T result) {
        if(result == null){
            throw new FrameException(FrameException.BASE_ERROR,"结果集为空");
        }

        if(syncResult == null){
            synchronized (monitor){
                if(syncResult == null){
                    syncResult = new SyncResult<>();
                    syncResult.set(result);
                }

                notifyAll();
            }
        }
        //done只有在这里会变化,所以不进入synchronized里面也没什么关系
        this.done = true;


    }
}
