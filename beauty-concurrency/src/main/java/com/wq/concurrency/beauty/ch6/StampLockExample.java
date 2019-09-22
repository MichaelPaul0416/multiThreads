package com.wq.concurrency.beauty.ch6;

import java.util.concurrent.locks.StampedLock;

public class StampLockExample {

    private final StampedLock stampedLock = new StampedLock();

    private int x,y = 0;

    // 悲观写
    public void move(int detaX,int detaY){
        long stamp = stampedLock.writeLock();
        try{
            x += detaX;
            y += detaY;
        }finally {
            this.stampedLock.unlockWrite(stamp);
        }
    }

    // 乐观读,适合读多写少
    public void distanceForSnapShot(){
        /**
         * 乐观读的使用必须严格遵循以下流程
         */
        long stamp = this.stampedLock.tryOptimisticRead();
        // 先缓存快照
        int tempX = x;
        int tempY = y;

        // 再去校验
        if(!this.stampedLock.validate(stamp)){
            // 说明期间有线程更新过了x,y,导致乐观获取的stamp不对应了
            // 那就直接获取悲观读锁(适合读少写多)
            stamp = this.stampedLock.readLock();
            try {
                // 重新获取一份
                tempX = x;
                tempX = y;
            }finally {
                this.stampedLock.unlockRead(stamp);
            }
        }

        System.out.println(Math.sqrt(tempX * tempX + tempY * tempY));
    }

    // 锁升级
    public void moveIfAtOrigin(int newX,int newY){
        // 先获取读锁[可以是乐观读锁也可是悲观读锁]
        long stamp = this.stampedLock.readLock();

        try{
            while (x ==0 && y == 0){
                // 升级为写锁
                long ws = this.stampedLock.tryConvertToWriteLock(stamp);
                if(ws != 0L){
                    // 升级成功
                    stamp = ws;
                    this.x = newX;
                    this.y = newY;
                    break;
                }else {
                    // 直接获取写锁
                    this.stampedLock.unlockRead(stamp);
                    this.stampedLock.writeLock();
                }
            }
        }finally {
            this.stampedLock.unlock(stamp);
        }
    }
}
