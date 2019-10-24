package com.wq.concurrency.test.jmockit.demo.testInject;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import org.junit.Assert;
import org.junit.Test;

public class TestedAndInjectable {

    @Tested
    OrderService orderService;

    long testUserId = 123456;

    long itemId = 456789;

    @Injectable MailService mailService;

    @Injectable UserCheckService userCheckService;

    @Test
    public void testSubmitOrder() {
        new Expectations() {
            {
                mailService.sendMail(testUserId, anyString);
                result = true;

                userCheckService.check(testUserId);
                result = true;
            }
        };

        Assert.assertTrue(orderService.submitOrder(testUserId, itemId));
    }
}
