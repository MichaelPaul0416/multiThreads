package com.wq.distribute.lock.core;

public interface Lock {

    int TIMEOUT = 3;

    void lock(String key,Runnable task);
}
