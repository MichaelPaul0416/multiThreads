package wq.concurrency.readwrite;

import java.util.concurrent.TimeUnit;

/**
 * @Author: wangqiang20995
 * @Date:2018/12/15
 * @Description:
 * @Resource:
 */
public class DataResource {

    private ReadWriteLock readWriteLock;
    private char[] buffer;

    public DataResource(int initLength){
        this.buffer = new char[initLength];
        for(int i = 0;i<initLength;i++){
            this.buffer[i] = '*';
        }
        this.readWriteLock = new ReadWriteLock();
    }

    /**
     *
     * readMessage/writeMessage采用了Before/After的模式
     * doBefore()
     * try{
     *     execute()
     * }finally{
     *     doAfter
     * }
     *
     */

    public char[] readMessage() throws InterruptedException {
        char[] message;
        readWriteLock.acquireReadLock();
        try {
            message = doReadMessage();
        } finally {
            readWriteLock.releaseReadLock();
        }
        return message;
    }

    private char[] doReadMessage() {
        if (this.buffer == null) {
            return new char[0];
        }

        char[] pcy = new char[this.buffer.length];
        System.arraycopy(this.buffer, 0, pcy, 0, this.buffer.length);

        try {
            slow();
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + "--" + e.getMessage());
        }
        return pcy;

    }


    public boolean writeMessage(String message) throws InterruptedException {
        boolean status = false;
        this.readWriteLock.acquireWriteLock();
        try{
            status = doWriteMessage(message);
        } finally {
            this.readWriteLock.releaseWriteLock();
        }

        return status;
    }

    private boolean doWriteMessage(String message) throws InterruptedException {
        if(this.buffer == null){
            return false;
        }

        char[] source = message.toCharArray();
        this.buffer = new char[source.length];
        System.arraycopy(source,0,this.buffer,0,source.length);
        for(int i = 0;i<source.length;i++){
            this.slow();
        }
        return true;
    }
    private void slow() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(50);
    }
}
