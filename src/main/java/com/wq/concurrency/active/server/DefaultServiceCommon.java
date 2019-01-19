package com.wq.concurrency.active.server;

import com.wq.concurrency.active.ServiceCommon;

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

    public DefaultServiceCommon(){
        dateFormat = new SimpleDateFormat(DATE_FORMAT);
    }

    @Override
    public String dateTimeNow(String pattern) {
        String message;
        if(pattern == null || EMPTY_DATE_FORMAT.equals(pattern)){
            message = dateFormat.format(new Date());
        }else {
            dateFormat.applyPattern(pattern);
            message = dateFormat.format(new Date());
        }

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return message;


    }

    @Override
    public void saveLog2Db(String level, String message) {
        System.out.println(String.format("保存日志级别为[%S]的信息[%s]到数据库",level,message));
    }
}
