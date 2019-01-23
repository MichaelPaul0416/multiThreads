package com.wq.concurrency.active.client;

import com.wq.concurrency.active.ServiceCommon;

/**
 * @Author: wangqiang20995
 * @Date:2019/1/23
 * @Description:
 * @Resource:
 */
public class ClientLogWorker implements Runnable {

    private String worker;

    private ServiceCommon serviceCommon;

    public ClientLogWorker(String worker) {
        this.worker = worker;
        this.serviceCommon = ServiceCommonProxy.instanceProxy();
    }

    public ClientLogWorker(String worker,ServiceCommon serviceCommon){
        this.serviceCommon = serviceCommon;
        this.worker = worker;
    }


    @Override
    public void run() {
        this.serviceCommon.saveLog2Db("warn", "mysql warn");
        System.out.println(Thread.currentThread().getName() + "-->" + worker + " done");
    }
}
