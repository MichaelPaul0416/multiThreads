package com.wq.concurrency.active.framework;

import java.io.Serializable;

/**
 * @Author: wangqiang20995
 * @Date:2019/1/19
 * @Description:
 * @Resource:
 */
public class SyncResult<T extends Serializable> extends Result<T> implements Serializable{

    private T result;


    @Override
    public T get() {
        return result;
    }

    @Override
    public void set(T result) {
        this.result = result;
    }
}
