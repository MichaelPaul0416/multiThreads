package com.wq.distribute.lock.mysql.cmd;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class MysqlManager {
    private SqlSessionFactory sqlSessionFactory;

    private final Logger logger = LoggerFactory.getLogger(MysqlManager.class);

    public MysqlManager(String config) {
        Reader reader = null;
        try {
            reader = new InputStreamReader(Resources.getResourceAsStream(config));
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.error(e.getLocalizedMessage(),e);
                }
            }
        }
    }

    public SqlSession sqlSession(){
        return this.sqlSessionFactory.openSession();
    }
}
