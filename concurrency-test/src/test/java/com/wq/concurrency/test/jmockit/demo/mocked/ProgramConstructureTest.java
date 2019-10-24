package com.wq.concurrency.test.jmockit.demo.mocked;

import mockit.*;
import org.junit.Assert;
import org.junit.Test;


//JMockit的程序结构
public class ProgramConstructureTest {

    // 这是一个测试属性
    @Tested
    HelloJMockit helloJMockit;

    @Test
    public void test1(@Injectable InnerService innerService) {
        // 录制(Record)
        new Expectations() {
            {
                innerService.describe();
                result = "ok";
            }
        };
        // 重放(Replay)
        String msg = helloJMockit.sayHello();
        Assert.assertTrue(msg.equals("Chinese，JMockit! -> ok"));
        // 验证(Verification)
        new Verifications() {
            {
                innerService.describe();
                times = 1;
            }
        };
    }

    @Test
    public void test2(@Mocked HelloJMockit helloJMockit /* 这是一个测试参数 */) {
        // 录制(Record)
        new Expectations() {
            {
                helloJMockit.sayHello();
                // 期待上述调用的返回是"hello,david"，而不是返回"hello,JMockit"
                result = "hello,david";
            }
        };
        // 重放(Replay)
        String msg = helloJMockit.sayHello();
        Assert.assertTrue(msg.equals("hello,david"));
        // 验证(Verification)
        new Verifications() {
            {
                helloJMockit.sayHello();
                // 验证helloJMockit.sayHello()这个方法调用了1次
                times = 1;
            }
        };
    }
}
