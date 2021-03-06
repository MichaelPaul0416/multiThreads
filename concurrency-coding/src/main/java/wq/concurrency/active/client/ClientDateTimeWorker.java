package wq.concurrency.active.client;

import wq.concurrency.active.ServiceCommon;
import wq.concurrency.active.framework.result.Result;

/**
 * @Author: wangqiang20995
 * @Date:2019/1/23
 * @Description:
 * @Resource:
 */
public class ClientDateTimeWorker implements Runnable {

    private String worker;

    private ServiceCommon serviceCommon;

    public ClientDateTimeWorker(String worker) {
        this.serviceCommon = ServiceCommonProxy.instanceProxy();
        this.worker = worker;
    }

    public ClientDateTimeWorker(String worker,ServiceCommon serviceCommon){
        this.worker = worker;
        this.serviceCommon = serviceCommon;
    }

    @Override
    public void run() {
        Result<String> result = this.serviceCommon.dateTimeNow("yyyyMMdd-HHmmss", worker);
        System.out.println("["+Thread.currentThread().getName() + "] --> [result:" + result.get() + "] done");

    }
}
