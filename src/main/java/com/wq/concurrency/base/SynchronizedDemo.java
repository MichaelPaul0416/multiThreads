package com.wq.concurrency.base;

/**
 * @Author:wangqiang20995
 * @description:
 * @Date:2017/9/22
 */
public class SynchronizedDemo {
    public static void main(String[] args) {
        box b = new box(1,"hello");
        Thread t1 = new Thread(new task(b));
        Thread t2 = new Thread(new task(b));


        Thread t3 = new Thread(new taskStatic());
        Thread t4 = new Thread(new taskStatic());

//		t1.start();
//		t2.start();
        t3.start();
        t4.start();
    }
}

class taskStatic implements Runnable{

    @Override
    public void run() {
        // TODO Auto-generated method stub
        box.show();
    }

}
class task implements Runnable{
    box b ;

    public task(box b){
        this.b = b;
    }

    @Override
    public void run(){
        b.display();
    }
}
class box{
    int number;

    String describe;

    static int size;

    static String color;

    static{
        size = 1;

        color = "yellow";
    }

    public box(int number,String describe){
        this.number = number;

        this.describe = describe;
    }

    public void display(){
        System.out.println(Thread.currentThread().getName() + "进入方法display，该方法是中有非静态同步代码块");
        synchronized(this){
            System.out.println(Thread.currentThread().getName() + "访问数字--box'number " + number);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "访问描述--box'describe " + describe);
            this.number ++;
            System.out.println(Thread.currentThread().getName() + "即将退出同步代码块");
        }
    }

    public static void show(){
        System.out.println(Thread.currentThread().getName() + "进入方法show，该方法中有静态同步代码块");
        synchronized(box.class){
            System.out.println(Thread.currentThread().getName() + "访问数字--box'number " + size);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "访问描述--box'describe " + color);
            size ++;
            System.out.println(Thread.currentThread().getName() + "即将退出静态同步代码块");
        }
    }
}