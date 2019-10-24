package com.wq.concurrency.test.jmockit.demo.mocked;

public class InnerService {

    public String describe(){
        System.out.println("inner service");
        return "inner";
    }
}
