package com.wq.concurrency.active.server;

import com.wq.concurrency.active.ServiceCommon;
import com.wq.concurrency.active.framework.Result;
import com.wq.concurrency.active.framework.SyncResult;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: wangqiang20995
 * @Date:2019/1/19
 * @Description:active-object模式中，真正的业务实现类
 * @Resource:
 */
public class DefaultServiceCommon implements ServiceCommon {

    private static final String DATE_FORMAT = "yyyyMMdd HHmmss";

    private static final String EMPTY_DATE_FORMAT = "";

    private final SimpleDateFormat dateFormat;

    public DefaultServiceCommon() {
        dateFormat = new SimpleDateFormat(DATE_FORMAT);
    }

    @Override
    public Result<String> dateTimeNow(String pattern, String customerId) {
        Result<String> syncResult = new SyncResult<>();
        String message;
        if (pattern == null || EMPTY_DATE_FORMAT.equals(pattern)) {
            message = dateFormat.format(new Date());
        } else {
            dateFormat.applyPattern(pattern);
            message = dateFormat.format(new Date());
        }
        syncResult.set(message);
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        System.out.println("[" + Thread.currentThread().getName() + "] Server result for Customer[" + customerId + "] -> " + message);
        return syncResult;//返回的结果是同步结果


    }

    @Override
    public void saveLog2Db(String level, String message) {
        System.out.println(String.format("[%s] 保存日志级别为[%S]的信息[%s]到数据库", Thread.currentThread().getName(),
                level, message));
    }
}
