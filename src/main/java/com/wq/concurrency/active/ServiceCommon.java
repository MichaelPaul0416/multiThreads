package com.wq.concurrency.active;

/**
 * @Author: wangqiang20995
 * @Date:2019/1/19
 * @Description:
 * @Resource:
 */
public interface ServiceCommon {

    String dateTimeNow(String pattern);

    void saveLog2Db(String level,String message);
}
