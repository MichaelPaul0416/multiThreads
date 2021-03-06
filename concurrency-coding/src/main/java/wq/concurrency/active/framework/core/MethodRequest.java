package wq.concurrency.active.framework.core;

import wq.concurrency.active.framework.result.Result;

import java.io.Serializable;

/**
 * @Author: wangqiang20995
 * @Date:2019/1/19
 * @Description:将service的方法封装成一个请求对象
 * @Resource:
 */
public interface MethodRequest<R extends Serializable> {
    //简要封装一下目标方法的返回对象
    Result<R> execute();

    //直接返回目标方法的返回对象
    R executeAndReturnRealValue();

    String getMethodName();
}
