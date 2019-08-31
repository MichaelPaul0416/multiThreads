package wq.concurrency.active.framework.result;

import java.io.Serializable;

/**
 * @Author: wangqiang20995
 * @Date:2019/1/19
 * @Description:
 * @Resource:
 */
public class ResultResolver {

    public static <T extends Serializable> Result<T> resolverResultType(SyncInvoker sync, T result) {

        if (SyncInvoker.isASynchronized(sync)){
            ASyncResult<T> aSyncResult = new ASyncResult<>();
            aSyncResult.set(result);

            return aSyncResult;
        }

        SyncResult<T> syncResult = new SyncResult<>();
        syncResult.set(result);

        return syncResult;
    }
}
