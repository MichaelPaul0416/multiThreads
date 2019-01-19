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
    protected Serializable get() {
        return null;
    }

    @Override
    protected void set(Serializable result) {

    }
}
