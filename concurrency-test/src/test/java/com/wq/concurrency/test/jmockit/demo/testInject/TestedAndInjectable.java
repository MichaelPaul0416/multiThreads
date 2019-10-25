package com.wq.concurrency.test.jmockit.demo.testInject;

import mockit.*;
import org.junit.Assert;
import org.junit.Test;

public class TestedAndInjectable {

    @Tested
    OrderService orderService;

    long testUserId = 123456;

    long itemId = 3;


    @Test
    public void testSubmitOrder(@Injectable MailService mailService,@Injectable UserCheckService userCheckService) {
//    public void testSubmitOrder() {
        new Expectations() {
            {
                mailService.sendMail(testUserId, anyString);
                result = true;

                userCheckService.check(testUserId);
                result = true;
            }
        };

        // 被mock的方法服务，需要手动被调用一遍
        Assert.assertTrue(mailService.sendMail(testUserId,null));
        Assert.assertTrue(userCheckService.check(testUserId));
        Assert.assertTrue(orderService.submitOrder(testUserId, itemId));

        new Verifications(){
            {
                mailService.sendMail(testUserId,anyString);
                times = 1;

                userCheckService.check(testUserId);
                times = 1;
            }
        };
    }

//    @Test
//    public void testException(){
//        new Expectations(){
//            {
//                userCheckService.check(testUserId);
//                result = true;
//            }
//        };
//
//        Assert.assertTrue(orderService.checkException(itemId));
//
//        new Verifications(){
//            {
//                userCheckService.check(testUserId);
//
//                times = 1;
//            }
//        };
//    }
}
