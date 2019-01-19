package com.wq.concurrency.active.framework;

import java.io.Serializable;

/**
 * @Author: wangqiang20995
 * @Date:2019/1/19
 * @Description:用于接受方法返回值的对象封装
 * @Resource:
 */
public abstract class Result<T extends Serializable> implements Serializable{

    protected abstract T get();

    protected abstract void set(T result);
}
