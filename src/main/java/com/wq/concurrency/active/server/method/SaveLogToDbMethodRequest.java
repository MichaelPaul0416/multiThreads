package com.wq.concurrency.active.server.method;

import com.wq.concurrency.active.ServiceCommon;
import com.wq.concurrency.active.framework.*;

/**
 * @Author: wangqiang20995
 * @Date:2019/1/19
 * @Description:
 * @Resource:
 */
public class SaveLogToDbMethodRequest implements MethodRequest<VoidResult> {

    private String level;
    private String message;
    private ServiceCommon serviceCommon;

    //其实后续可以用注解注入或者xml注入
    public SaveLogToDbMethodRequest(ServiceCommon serviceCommon,String level,String message) {
        this.level = level;
        this.message = message;

        this.serviceCommon = serviceCommon;
    }

    @Override
    public Result<VoidResult> execute() {

        this.serviceCommon.saveLog2Db(level,message);

        ResultResolver.resolverResultType(SyncInvoker.valueOf(sync),new VoidResult());
        return null;
    }

    @Override
    public VoidResult executeAndReturnOrigin() {
        this.serviceCommon.saveLog2Db(level, message);

        return new VoidResult();
    }
}
