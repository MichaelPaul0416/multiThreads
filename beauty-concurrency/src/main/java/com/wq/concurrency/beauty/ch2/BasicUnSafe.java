package com.wq.concurrency.beauty.ch2;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class BasicUnSafe {

    static final Unsafe unsafe ;

    //记录state变量在BasicUnsafe中的偏移量
    static final long offset;

    private volatile long state = 0;

    static{
        try{
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe) field.get(null);
            offset = unsafe.objectFieldOffset(BasicUnSafe.class.getDeclaredField("state"));
        }catch (Exception e){
            System.out.println(e.getMessage());
            throw new Error(e);
        }
    }

    public static void main(String[] args){
        BasicUnSafe basicUnSafe = new BasicUnSafe();
        Boolean success = unsafe.compareAndSwapLong(basicUnSafe,offset,0,1);
        System.out.println(success);
    }
}
