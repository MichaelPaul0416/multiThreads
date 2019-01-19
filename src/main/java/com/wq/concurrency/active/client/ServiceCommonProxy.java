package com.wq.concurrency.active.client;

import com.wq.concurrency.active.ServiceCommon;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Author: wangqiang20995
 * @Date:2019/1/19
 * @Description:
 * @Resource:
 */
public class ServiceCommonProxy implements ServiceCommon {
    @Override
    public String dateTimeNow(String pattern) {
        return null;
    }

    @Override
    public void saveLog2Db(String level, String message) {

    }

    private class InnerProxyHander implements InvocationHandler{


        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return null;
        }
    }
}
