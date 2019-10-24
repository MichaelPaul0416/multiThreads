package com.wq.concurrency.test.jmockit.demo.testInject;

public class OrderService {

    MailService mailService;

    UserCheckService userCheckService;

    public boolean submitOrder(long buyerId,long itemId){
        if(!userCheckService.check(buyerId)){
            return false;
        }

        if (!this.mailService.sendMail(buyerId,"下单成功")){
            return false;
        }

        return true;
    }
}
