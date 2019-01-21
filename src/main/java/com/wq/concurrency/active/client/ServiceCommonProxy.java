package com.wq.concurrency.active.client;

import com.wq.concurrency.active.ServiceCommon;
import com.wq.concurrency.active.framework.ASyncResult;
import com.wq.concurrency.active.framework.MethodRequest;
import com.wq.concurrency.active.framework.SchedulerHandler;
import com.wq.concurrency.active.server.method.DateTimeMethodRequest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Author: wangqiang20995
 * @Date:2019/1/19
 * @Description:
 * @Resource:
 */
public class ServiceCommonProxy implements ServiceCommon {

    private static final Integer QUEUE_LENGTH = 100;

    private SchedulerHandler schedulerHandler;

    private ServiceCommon coreServiceCommon;

    private ASyncResult aSyncResult;

    public ServiceCommonProxy(ServiceCommon coreServiceCommon,ASyncResult aSyncResult){
        this.coreServiceCommon = coreServiceCommon;
        this.aSyncResult =  aSyncResult;
        schedulerHandler = new SchedulerHandler(QUEUE_LENGTH);
        schedulerHandler.start();
    }

    @Override
    public String dateTimeNow(String pattern) {
        MethodRequest<String> methodRequest = new DateTimeMethodRequest(pattern,this.coreServiceCommon,aSyncResult);
        return methodRequest.execute();
    }

    @Override
    public void saveLog2Db(String level, String message) {

    }


    public static ServiceCommon instance(){

    }

    private class InnerProxyHander implements InvocationHandler{


        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return null;
        }
    }
}
