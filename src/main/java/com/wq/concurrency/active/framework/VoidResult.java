package com.wq.concurrency.active.framework;

import java.io.Serializable;

/**
 * @Author: wangqiang20995
 * @Date:2019/1/19
 * @Description:
 * @Resource:
 */
public class VoidResult extends Result implements Serializable{

    @Override
    public Serializable get() {
        return null;
    }

    @Override
    public void set(Serializable result) {

    }
}
