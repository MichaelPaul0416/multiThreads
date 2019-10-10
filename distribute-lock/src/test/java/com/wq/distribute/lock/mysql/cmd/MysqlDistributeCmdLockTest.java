package com.wq.distribute.lock.mysql.cmd;

import com.wq.distribute.lock.core.Lock;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MysqlDistributeCmdLockTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private MysqlManager mysqlManager;

    private Lock lock;

    @Before
    public void init(){
        mysqlManager = new MysqlManager("db/mybatis-config.xml");
        lock = new MysqlDistributeCmdLock(mysqlManager);
    }

    @Test
    public void testGetLock(){
        lock.lock("hello", new Runnable() {
            @Override
            public void run() {
                System.out.println("err");
            }
        });
    }
    @Test
    public void test(){
        logger.info("hello");
    }
}