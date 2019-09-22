package com.wq.concurrency.beauty.ch6;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ThreadSafeList<T> {

    private final List<T> container = new ArrayList<>();

    private final boolean rwlock;

    private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();

    private ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();

    private volatile Lock lock;

    public ThreadSafeList(boolean rwlock) {
        this.lock = new ReentrantLock(false);
        this.rwlock = rwlock;
    }

    public void add(T t) {
        if (rwlock) {
            writeLock.lock();
        } else {
            lock.lock();
        }

        try {
            this.container.add(t);
        } finally {
            if (rwlock) {
                writeLock.unlock();
            } else {
                lock.unlock();
            }
        }
    }

    public T get(int index) {
        if (rwlock) {
            readLock.lock();
        } else {
            lock.lock();
        }
        try {
            if (index >= this.container.size()) {
                throw new ArrayIndexOutOfBoundsException();
            }

            return this.container.get(index);
        } finally {
            if (rwlock) {
                readLock.unlock();
            } else {
                lock.unlock();
            }
        }
    }

    public void remove(int index) {
        if(rwlock){
            writeLock.lock();
        }else {
            lock.lock();
        }
        try {
            if (index >= this.container.size()) {
                throw new ArrayIndexOutOfBoundsException();
            }

            this.container.remove(index);
        } finally {
            if(rwlock){
                writeLock.unlock();
            }else {
                lock.unlock();
            }
        }
    }
}
