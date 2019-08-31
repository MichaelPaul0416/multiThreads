package wq.concurrency.active.server.method;

import com.wq.concurrency.active.ServiceCommon;
import com.wq.concurrency.active.framework.core.MethodRequest;
import com.wq.concurrency.active.framework.result.Result;
import com.wq.concurrency.active.framework.result.ResultResolver;
import com.wq.concurrency.active.framework.result.SyncInvoker;
import com.wq.concurrency.active.framework.result.VoidResult;

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

    private static final String METHOD_NAME = "saveLog2Db";

    //其实后续可以用注解注入或者xml注入
    public SaveLogToDbMethodRequest(ServiceCommon serviceCommon,String level,String message) {
        this.level = level;
        this.message = message;

        this.serviceCommon = serviceCommon;
    }

    @Override
    public Result<VoidResult> execute() {

        this.serviceCommon.saveLog2Db(level,message);

        Result<VoidResult> resultResult =
                ResultResolver.resolverResultType(SyncInvoker.DEFAULT_INVOKER,new VoidResult());
        return resultResult;
    }

    @Override
    public VoidResult executeAndReturnRealValue() {
        this.serviceCommon.saveLog2Db(level, message);

        return new VoidResult();
    }

    @Override
    public String getMethodName() {
        return METHOD_NAME;
    }
}
