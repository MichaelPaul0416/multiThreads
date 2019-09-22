package wq.concurrency.active.server.method;

import org.apache.commons.lang3.StringUtils;
import wq.concurrency.active.ServiceCommon;
import wq.concurrency.active.framework.FrameException;
import wq.concurrency.active.framework.core.MethodRequest;
import wq.concurrency.active.framework.result.ASyncResult;
import wq.concurrency.active.framework.result.Result;

/**
 * @Author: wangqiang20995
 * @Date:2019/1/19
 * @Description:
 * @Resource:
 */
public class DateTimeMethodRequest implements MethodRequest<String> {

    private String pattern;

    private String customerId;

    private static final String METHOD_NAME = "dateTimeNow";

    private Class<?> belongClass;

    private ServiceCommon actualServiceCommon;

    private ASyncResult<String> result;

    public DateTimeMethodRequest(String customerId,String pattern,ServiceCommon core,ASyncResult<String> result){
        this.pattern = pattern;
        this.actualServiceCommon = core;
        this.result = result;
        this.customerId = customerId;

        this.belongClass = this.actualServiceCommon.getClass();
    }

    @Override
    public Result<String> execute() {
        Result<String> message = actualServiceCommon.dateTimeNow(pattern, this.customerId);
//        暂时先注释掉，后序有好的思路再加上
//        return ResultResolver.resolverResultType(SyncInvoker.valueOf(sync),result);

        this.result.set(message.get());//将SyncResult的结果值存入ASyncResult中

        return this.result;

    }

    @Override
    public String executeAndReturnRealValue() {
        if(StringUtils.isEmpty(pattern)){
            throw new FrameException(FrameException.SERVICE_ERROR,"入参为空");
        }

        //此处以后可以用动态代理进行一些增强拦截
        Result<String> result = actualServiceCommon.dateTimeNow(pattern, customerId);

        //睡1秒模拟网络延迟
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result.get();
    }

    @Override
    public String getMethodName() {
        return METHOD_NAME;
    }
}
