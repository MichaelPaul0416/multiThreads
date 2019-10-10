package com.wq.distribute.lock.core;

import java.io.Serializable;

public class LockParam implements Serializable {

    private static final long serialVersionId = 1L;

    public LockParam() {
    }

    public LockParam(String key, int timeout) {
        this.key = key;
        this.timeout = timeout;
    }

    private String key;
    private int timeout;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
