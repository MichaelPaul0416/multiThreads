package com.wq.concurrency.nio;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @Author: wangqiang20995
 * @Date:2019/4/21
 * @Description:
 * @Resource:
 */
public class UnsafeDemo {

    public static void main(String[] args){
        InnerWakeUpClass innerWakeUpClass = new InnerWakeUpClass(1000 * 1000);
        Thread thread = new Thread(innerWakeUpClass);
        thread.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            System.out.println("main线程休眠异常");
        }finally {
            System.out.println("准备唤醒子线程:" + thread.getName());
//            innerWakeUpClass.wakeByUnsafe(thread);
            innerWakeUpClass.wakeByInterrupt(thread);
        }

        System.out.println("done");


        System.out.println("unsafe修改字段值");

        ModifyWithUnsafe modify = new ModifyWithUnsafe();

        if(modify.compareAndSetState(0,1)){
            System.out.println("modify.state:" + modify.getState());
        }
    }

    private static class ModifyWithUnsafe{
        private static long stateOffset = 0L;

        private static Unsafe unsafe = null;

        private static int state = 0;

        static {
            try {
                unsafe = unsafe();
                stateOffset = unsafe.objectFieldOffset(ModifyWithUnsafe.class.getDeclaredField("state"));
            }catch (Exception e){
                System.out.println("获取unsafe异常:" + e.getMessage());
                e.printStackTrace();
            }
        }


        public int getState(){
            return this.state;
        }

        public boolean compareAndSetState(int expect,int update){
            return unsafe.compareAndSwapInt(this,stateOffset,expect,update);
        }
    }

    private static class InnerWakeUpClass implements Runnable{

        private long wait;
        private static Unsafe unsafe = null;

        static {
            try {
                unsafe = unsafe();
            } catch (Exception e) {
                System.out.println("获取unsafe异常");
            }
        }

        public InnerWakeUpClass(long wait){
            this.wait = wait;
        }

        @Override
        public void run() {
            System.out.println("调用Unsafe阻塞当前线程");
            try {
                unsafe.park(false, this.wait);
            }catch (Exception e){
                System.out.println("Exception:" + e.getMessage());
            }
        }

        public void wakeByUnsafe(Thread thread){
            System.out.println("使用unsafe唤醒阻塞的线程");
            unsafe.unpark(thread);
        }

        public void wakeByInterrupt(Thread thread){
            System.out.println("使用线程本身的interrupt()唤醒线程");
            thread.interrupt();
        }
    }

    private static Unsafe unsafe() throws Exception{
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            Unsafe unsafe = (Unsafe) field.get(null);

            return unsafe;
    }
}
