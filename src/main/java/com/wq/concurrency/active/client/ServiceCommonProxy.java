package com.wq.concurrency.active.client;

import com.wq.concurrency.active.ServiceCommon;
import com.wq.concurrency.active.framework.core.MethodRequest;
import com.wq.concurrency.active.framework.core.SchedulerHandler;
import com.wq.concurrency.active.framework.result.ASyncResult;
import com.wq.concurrency.active.framework.result.Result;
import com.wq.concurrency.active.framework.result.VoidResult;
import com.wq.concurrency.active.server.DefaultServiceCommon;
import com.wq.concurrency.active.server.method.DateTimeMethodRequest;
import com.wq.concurrency.active.server.method.SaveLogToDbMethodRequest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

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

    public ServiceCommonProxy(ServiceCommon coreServiceCommon) {
        this(coreServiceCommon, new SchedulerHandler(QUEUE_LENGTH));
    }

    public ServiceCommonProxy(ServiceCommon serviceCommon, SchedulerHandler schedulerHandler) {
        this.coreServiceCommon = serviceCommon;
        this.schedulerHandler = schedulerHandler;
        this.schedulerHandler.setName("Scheduler Thread");
        if (this.schedulerHandler.getState().equals(Thread.State.NEW)) {
            this.schedulerHandler.start();
        }
    }

    @Override
    public Result<String> dateTimeNow(String pattern, String customerId) {
        ASyncResult<String> aSyncResult = new ASyncResult<>();
        MethodRequest<String> methodRequest = new DateTimeMethodRequest(customerId,
                pattern, this.coreServiceCommon, aSyncResult);
        schedulerHandler.invoke(methodRequest);
        return aSyncResult;
    }

    @Override
    public void saveLog2Db(String level, String message) {
        MethodRequest<VoidResult> voidResult = new SaveLogToDbMethodRequest(this.coreServiceCommon, level, message);
        schedulerHandler.invoke(voidResult);
    }


    public static ServiceCommon instanceProxy() {
        ServiceCommon realServiceCommon = new DefaultServiceCommon();
        ServiceCommon proxyServiceCommon = (ServiceCommon) Proxy.newProxyInstance(
                ServiceCommonProxy.class.getClassLoader(), new Class[]{ServiceCommon.class},
                new InnerProxyHandler(new ServiceCommonProxy(realServiceCommon)));
        return proxyServiceCommon;
    }

    public static ServiceCommon instanceProxy(SchedulerHandler schedulerHandler) {
        ServiceCommon realServiceCommon = new DefaultServiceCommon();
        ServiceCommon proxyServiceCommon = (ServiceCommon) Proxy.newProxyInstance(
                ServiceCommonProxy.class.getClassLoader(), new Class[]{ServiceCommon.class},
                new InnerProxyHandler(new ServiceCommonProxy(realServiceCommon, schedulerHandler))
        );

        return proxyServiceCommon;
    }

    private static class InnerProxyHandler implements InvocationHandler {

        private ServiceCommon proxy;//对静态代理对象进行拦截

        public InnerProxyHandler(ServiceCommon serviceCommon) {
            this.proxy = serviceCommon;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//            System.out.println("[" + Thread.currentThread().getName() + "] handler pre check");
            Object object = method.invoke(this.proxy, args);
//            System.out.println("[" + Thread.currentThread().getName() + "] handler after check");
            return object;
        }
    }
}
