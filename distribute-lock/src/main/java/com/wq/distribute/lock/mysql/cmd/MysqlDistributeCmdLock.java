package com.wq.distribute.lock.mysql.cmd;

import com.sun.org.apache.bcel.internal.generic.ATHROW;
import com.wq.distribute.lock.core.Lock;
import com.wq.distribute.lock.core.LockParam;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 基于mysql实现的分布式锁
 * 使用mysql提供的get_lock(key,timeout)/release_lock(key)完成加锁和释放锁的动作
 * 可重入的特性基于单一session实现，同一个session可以多次get_lock实现重入
 * 一个客户端去获取分布式锁 对应的就是 客户端向mysql的一个session
 * 同一个线程不能判定为可重入，必须是同一个session才是可重入
 * 只要是get_lock方法返回了1，那么这个key的有效期就一直到session失效（客户端断开或者崩溃，会自动销毁这个key）
 */
public class MysqlDistributeCmdLock implements Lock {

    private final MysqlManager mysqlManager;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final ThreadLocal<SqlSessionWrapper> cacheSqlSession = new ThreadLocal<>();

    private static final String LOCKED = "LockerMapper.getLock";

    private static final String RELEASE = "LockerMapper.releaseLock";

    public MysqlDistributeCmdLock(MysqlManager mysqlManager){
        this.mysqlManager = mysqlManager;
    }

    @Override
    public void lock(String key, Runnable task) {
        SqlSessionWrapper wrapper = cacheSqlSession.get();
        if(wrapper == null){
            cacheSqlSession.set(new SqlSessionWrapper(mysqlManager.sqlSession()));
        }

        // get lock
        try{
            if(getLock(key,TIMEOUT)){
                task.run();
            }
        }catch (Throwable e){
            logger.error(e.getLocalizedMessage(),e);
        }finally {

        }
    }

    private boolean releaseLock(String key){
        SqlSessionWrapper wrapper = cacheSqlSession.get();
        if(wrapper == null){
            return false;
        }

        boolean unlock = false;
        try{
            Integer release = wrapper.sqlSession.selectOne(RELEASE,key);
            if(release == null){
                logger.warn("release key not exist...");
            }

            if(release.intValue() == 0){
                logger.error("key [{}] is not hold by current session:{}",key,wrapper.sqlSession);
            }

            if(release.intValue() >= 1){
                wrapper.state --;
                unlock = true;
                if(wrapper.state == 0) {
                    wrapper.sqlSession.close();
                    cacheSqlSession.remove();
                }
            }
        }catch (Throwable e){
            logger.error(e.getLocalizedMessage(),e);
        }
        return unlock;
    }



    private boolean getLock(String key,int timeout){
        LockParam lockParam = new LockParam(key,timeout);
        SqlSessionWrapper wrapper = cacheSqlSession.get();

        try {
            Integer success = wrapper.sqlSession.selectOne(LOCKED, lockParam);
            if(success == null || success.intValue() == 0){
                return false;
            }

            wrapper.state ++;
            return true;
        }catch (Exception e){
            logger.error(e.getLocalizedMessage(),e);
            return false;
        }

    }
    private static class SqlSessionWrapper {
        private SqlSession sqlSession;
        private int state;

        public SqlSessionWrapper(SqlSession sqlSession) {
            this.sqlSession = sqlSession;
            this.state = 0;
        }
    }

}
