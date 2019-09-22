package wq.concurrency.active.framework.result;

import wq.concurrency.active.framework.FrameException;

import java.io.Serializable;

/**
 * @Author: wangqiang20995
 * @Date:2019/1/19
 * @Description:异步结果接收对象
 * @Resource:
 */
public class ASyncResult<T extends Serializable> extends Result<T> implements Serializable {

    private SyncResult<T> syncResult;

    private volatile boolean done = false;

    private Object monitor = new Object();

    @Override
    public T get() {
        synchronized (monitor) {
            while (!done) {
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    System.out.println(Thread.currentThread().getName() + "休眠状态被打断，检查结果是否已经准备好了");
                }
            }
            System.out.println("[" + Thread.currentThread().getName() + "] GET message of [" + this + "]");
            return syncResult.get();
        }
    }

    @Override
    public void set(T result) {
        if (result == null) {
            throw new FrameException(FrameException.BASE_ERROR, "结果集为空");
        }

        synchronized (monitor) {
            if (syncResult == null) {
                syncResult = new SyncResult<>();
            }
            syncResult.set(result);
            System.out.println("[" + Thread.currentThread().getName() + "] set message of [" + this + "]");
            monitor.notifyAll();

            //状态设置必须在synchronized里面，加入在synchronized外面的话，当程序执行完同步代码块之后，线程切换了
            //然后调用get方法的线程原来是阻塞在monitor的队列上的，此时被唤醒，重新进去while的判断条件，但是由于this.done还没来得及执行，所以继续wait
            //但是monitor.notifyAll()已经被执行过了，而且set不会再被执行，所以这个线程就一直阻塞在这里
            //所以this.done=true这个改变状态的代码必须在synchronized里面
            this.done = true;
        }


    }
}
