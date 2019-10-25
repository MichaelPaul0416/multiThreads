package com.wq.concurrency.test.jmockit.demo.testInject;

public class OrderService {

    MailService mailService = new MailService() {
        @Override
        public boolean sendMail(long userId, String content) {
            return userId > 0;
        }
    };

    UserCheckService userCheckService = new UserCheckService() {
        @Override
        public boolean check(long userId) {
            return userId > 10;
        }
    };

//    MailService mailService;
//
//    UserCheckService userCheckService;

    public boolean submitOrder(long buyerId,long itemId){
        if(!userCheckService.check(buyerId)){
            return false;
        }

        if (!this.mailService.sendMail(buyerId,"下单成功")){
            return false;
        }

        return true;
    }

    public boolean checkException(long buyerId){
        userCheckService.check(buyerId);

        if(buyerId > 1){
            throw new RuntimeException("1");
        }

        return true;
    }
}
