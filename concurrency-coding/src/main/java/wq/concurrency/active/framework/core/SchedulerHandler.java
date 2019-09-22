package wq.concurrency.active.framework.core;

import java.io.Serializable;

/**
 * @Author: wangqiang20995
 * @Date:2019/1/19
 * @Description:
 * @Resource:
 */
public class SchedulerHandler extends Thread {

    private SimpleSaveQueue simpleSaveQueue;

    private volatile boolean stop = false;

    public SchedulerHandler(int length) {
        simpleSaveQueue = new SimpleSaveQueue(length);
    }


    public <T extends Serializable> boolean invoke(MethodRequest<T> methodRequest) {
        try {
            System.out.println("[" + Thread.currentThread().getName() + "] 提交方法[" + methodRequest.getMethodName() + "]的请求");
            this.simpleSaveQueue.set(methodRequest);
            return true;
        } catch (InterruptedException e) {
            System.out.println("请求加入安全队列失败");
            return false;
        }
    }

    public void stopHandler() {
        try {
            while (!this.simpleSaveQueue.empty()) {//自旋锁，一直等到队列为空
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            System.out.println("提交停止Scheduler线程处理请求失败");
        }

        this.stop = true;
        interrupt();//当前线程置为中断状态
    }

    @Override
    public void run() {

        while (!stop) {
            try {
                MethodRequest<Serializable> methodRequest = simpleSaveQueue.get();
                methodRequest.execute();

                //模拟执行完一个task之后需要休息一下
                Thread.sleep(2500);
            } catch (InterruptedException e) {
                if (this.simpleSaveQueue.empty()) {
                    System.out.println("[" + Thread.currentThread().getName() + "] Scheduler 线程任务执行完毕，准备结束...");
                } else {
                    System.out.println("[" + Thread.currentThread().getName() + "] Scheduler 线程休眠时被中断，由于没有执行完提交的任务，所以继续执行");
                }
            }
        }

        System.out.println("[" + Thread.currentThread().getName() + "] 执行完毕，即将关闭......");
    }
}
