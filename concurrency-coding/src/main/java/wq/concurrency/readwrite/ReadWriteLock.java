package wq.concurrency.readwrite;

/**
 * @Author: wangqiang20995
 * @Date:2018/12/15
 * @Description:读写锁
 * @Resource:
 */

/**
 * 关于逻辑锁的一些感想：和物理锁最大的不同在于物理锁锁的是被操作的共享资源对象，属于比较直观的锁，线程A、B都想操作资源C，那么就需要用物理锁对资源C上锁
 * 但是逻辑锁可能有一点绕，线程A、B都同时操作资源C，资源C调用锁D，D虽然也用synchronized，但其实锁的不是信息数据，synchronized只是为了保证一些全局信息的安全性，不出现并发问题
 * 真正的锁的功能其实是依靠全局信息来实现，while(!condition){monitor.wait()},当condition成立之后，操作全局信息，然后调用notify/notifyAll通知所有在monitor上阻塞的线程，进行下一轮操作
 */
public class ReadWriteLock {

    private int readerNumber;//正在读取的线程数量
    private int writerNumber;//正在写入的线程数量
    private int waiterNumber;//等待读取/写入的线程数量

    private boolean porityWrite;//是否写优先

    private final Object monitor = new Object();//synchronized 的锁对象

    /**
     * 1.读线程与读线程共同读取一个资源不会冲突
     * 2.读线程读取的时候，写线程应该阻塞
     * 3.写线程写入的时候，其他所有的读线程和写线程都应该阻塞
     */


    public void acquireReadLock() throws InterruptedException {
        /**
         * 读线程能顺利获取的情况【没有其他写线程正在写入】
         * 正在写入的线程<=0 && 没有正在等待的写入的线程
         */
        synchronized (monitor) {
            String thread = Thread.currentThread().getName();
            //取反-->当前读线程阻塞，需要等待
            while (writerNumber > 0 || (porityWrite && waiterNumber > 0)) {
                this.monitor.wait();//一定是要synchronized锁住的对象的wait/notify/notifyAll方法，不然白搞
            }
            this.readerNumber++;
            System.out.println(String.format("当前线程[%s]获取读锁", thread));
        }
    }

    public void releaseReadLock() {
        synchronized (monitor) {
            String thread = Thread.currentThread().getName();

            //读锁应该是无条件释放的
            this.readerNumber--;
            this.porityWrite = true;
            this.monitor.notifyAll();
            System.out.println(String.format("当前线程[%s]释放了读锁",thread));
        }
    }

    public void acquireWriteLock() throws InterruptedException {
        /**
         * 写锁能获得的条件：不存在写线程和读线程
         */
        synchronized (monitor) {
            /**
             * 使用before/after
             * before()
             * try{
             *     execute()
             * }finally{
             *     after()
             * }
             * 保证了before执行之后，after一定能执行，即使execute发生异常
             */
            this.waiterNumber++;
            String thread = Thread.currentThread().getName();
            try {
                while (this.readerNumber > 0 || this.writerNumber > 0) {
                    this.monitor.wait();
                }
                this.writerNumber++;
            }finally {
                this.waiterNumber--;
            }
            System.out.println(String.format("当前线程[%s]获取了写锁",thread));
        }
    }

    public void releaseWriteLock(){
        //写锁直接释放
        synchronized (monitor){
            String thread = Thread.currentThread().getName();
            this.writerNumber --;
            this.porityWrite = false;
            this.monitor.notifyAll();
            System.out.println(String.format("当前线程[%s]释放了写锁",thread));
        }
    }

}
