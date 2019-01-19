package com.wq.concurrency.active.server.method;

import com.wq.concurrency.active.ServiceCommon;
import com.wq.concurrency.active.framework.*;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: wangqiang20995
 * @Date:2019/1/19
 * @Description:
 * @Resource:
 */
public class DateTimeMethodRequest implements MethodRequest<String> {

    private String pattern;

    private static final String METHOD_NAME = "dateTimeNow";

    private Class<?> belongClass;

    private ServiceCommon actualServiceCommon;

    public DateTimeMethodRequest(String pattern,ServiceCommon core){
        this.pattern = pattern;
        this.actualServiceCommon = core;

        this.belongClass = this.actualServiceCommon.getClass();
    }

    @Override
    public Result<String> execute(boolean sync) {
        String result = actualServiceCommon.dateTimeNow(pattern);
        return ResultResolver.resolverResultType(SyncInvoker.valueOf(sync),result);

    }

    @Override
    public String executeAndReturnOrigin() {
        if(StringUtils.isEmpty(pattern)){
            throw new FrameException(FrameException.SERVICE_ERROR,"入参为空");
        }

        //此处以后可以用动态代理进行一些增强拦截
        String result = actualServiceCommon.dateTimeNow(pattern);

        //睡1秒模拟网络延迟
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result;
    }
}
