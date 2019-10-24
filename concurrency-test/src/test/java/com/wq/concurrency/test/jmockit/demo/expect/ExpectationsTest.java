package com.wq.concurrency.test.jmockit.demo.expect;

import mockit.Expectations;
import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

public class ExpectationsTest {

    @Test
    public void test1() {
        Calendar cal = Calendar.getInstance();
        // 把待Mock的类传入Expectations的构造函数，可以达到只mock类的部分行为的目的
        new Expectations(Calendar.class) {
            {
                // 只对get方法并且参数为Calendar.HOUR_OF_DAY进行录制
                cal.get(Calendar.HOUR_OF_DAY);
                result = 7;// 小时永远返回早上7点钟
            }
        };
        Calendar now = Calendar.getInstance();
        // 因为下面的调用mock过了，小时永远返回7点钟了
        Assert.assertTrue(now.get(Calendar.HOUR_OF_DAY) == 7);
        // 因为下面的调用没有mock过，所以方法的行为不受mock影响，
        Assert.assertTrue(now.get(Calendar.DAY_OF_MONTH) == (new Date()).getDate());
    }

    @Test
    public void test2(){
        Calendar calendar = Calendar.getInstance();
        // 将其作为构造器入参传入的时候，作用域就是当前实例对象
        new Expectations(calendar){
            {
                calendar.get(Calendar.HOUR_OF_DAY);
                result = 3;
            }
        };

        Calendar c2 = Calendar.getInstance();
        Assert.assertTrue(calendar.get(Calendar.HOUR_OF_DAY) == 3);
        Assert.assertTrue(c2.get(Calendar.HOUR_OF_DAY) != 3);
    }
}
