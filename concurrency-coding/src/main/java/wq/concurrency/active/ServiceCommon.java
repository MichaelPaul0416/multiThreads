package wq.concurrency.active;

import wq.concurrency.active.framework.result.Result;

/**
 * @Author: wangqiang20995
 * @Date:2019/1/19
 * @Description:
 * @Resource:
 */
public interface ServiceCommon {

    Result<String> dateTimeNow(String pattern, String customerId);

    void saveLog2Db(String level, String message);
}
