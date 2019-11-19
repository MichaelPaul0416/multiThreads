package com.wq.concurrency.beauty;

import sun.misc.Unsafe;
import sun.nio.ch.DirectBuffer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;

/**
 * @Author: wangqiang20995
 * @Date: 2019/11/19 19:42
 * @Description:
 **/
public class UnsafeExample {
    private static Unsafe UNSAFE;

    private static ByteBuffer DIRECT_BYTE_BUFFER;

    public static void main(String[] args) {
        UNSAFE = obtainUnSafe();
        if (UNSAFE == null) {
            System.out.println("empty Unsafe");
            return;
        }

        int capacity = 10;
        long start = UNSAFE.allocateMemory(capacity);
        System.out.println(capacity + " byte[] and start with:" + start);

        // 调用DirectByteBuffer 获取堆外内存对象
        DIRECT_BYTE_BUFFER = directByteBuffer(capacity, start);
        if (DIRECT_BYTE_BUFFER == null) {
            System.out.println("empty direct byte buffer");
            return;
        }

        // write
        for (int i = 0; i < capacity; i++) {
            DIRECT_BYTE_BUFFER.put((byte) ('A' + i));
        }

        // reallocate
        int newCapacity = capacity * 2;
        start = UNSAFE.reallocateMemory(start, newCapacity);
        System.out.println("new direct byte buffer start with:" + start);
        for (int i = capacity; i < newCapacity; i++) {
            DIRECT_BYTE_BUFFER.put((byte) ('A' + i));
        }

        // write -> read
        DIRECT_BYTE_BUFFER.flip();
        byte[] data = DIRECT_BYTE_BUFFER.array();
        System.out.println(new String(data));

        assert DIRECT_BYTE_BUFFER.remaining() == 0;
    }

    private static ByteBuffer directByteBuffer(int capacity, long start) {
        Class<ByteBuffer> bufferClass = ByteBuffer.class;
        try {
            Constructor constructor = bufferClass.getDeclaredConstructor(long.class, int.class);
            return (ByteBuffer) constructor.newInstance(start, capacity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Unsafe obtainUnSafe() {
        Class<Unsafe> clazz = Unsafe.class;
        try {
            Field field = clazz.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            return (Unsafe) field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }
}
