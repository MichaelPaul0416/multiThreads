package wq.concurrency.active.framework.result;

/**
 * @Author: wangqiang20995
 * @Date:2019/1/19
 * @Description:
 * @Resource:
 */
public enum SyncInvoker {

    SYNC_INVOKER(true),
    ASYNC_INVOKER(false),
    DEFAULT_INVOKER(false);

    private boolean status;

    SyncInvoker(boolean status){
        this.status = status;
    }

    public static synchronized boolean isASynchronized(SyncInvoker invoker){
        for(SyncInvoker syncInvoker : SyncInvoker.values()){
            if(invoker.status == syncInvoker.status){
                return syncInvoker.status;
            }
        }

        return false;
    }

    public static synchronized SyncInvoker valueOf(boolean value){
        for(SyncInvoker syncInvoker : SyncInvoker.values()){
            if(syncInvoker.status == value){
                return syncInvoker;
            }
        }

        return DEFAULT_INVOKER;
    }

}
