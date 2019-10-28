package com.wq.concurrency.test.jmockit.demo.mockup;

import com.wq.concurrency.test.jmockit.demo.testInject.MailService;
import junit.framework.Assert;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import org.junit.Test;

import java.time.LocalDateTime;

/**
 * @Author: wangqiang20995
 * @Date: 2019/10/28 11:00
 * @Description:
 **/
public class DateUtilsTest {
    @Mocked
    DateUtils dateUtils;

    @Mocked
    MailService mailService;

    private LocalDateTime localDateTime = LocalDateTime.now();

    @Test
    public void testStaticMethod() {
//        new DateExpectations(dateUtils);
        mock();
        new Expectations() {
            {
                mailService.sendMail(1,"hello");
                result = true;
            }
        };

        DatetimeWrapper wrapper = DatetimeWrapper.newInstance();
        System.out.println(wrapper);
        Assert.assertEquals(20191027, DateUtils.getDay().intValue());
        Assert.assertEquals(000000, DateUtils.getTime().intValue());
        Assert.assertTrue(mailService.sendMail(1,"hello"));

        new Verifications() {
            {
                dateUtils.getDay();
                times = 2;

                dateUtils.getTime();
                times = 2;
            }
        };
    }

    private void mock() {
        new Expectations(){
            {
                dateUtils.getDay();
                result = 20191027;

                dateUtils.getTime();
                result = 000000;

                // mock有参数的方法的时候，需要使用withAny将其转换为mock的入参，不然直接传入的话，是mock不到指定方法的
                dateUtils.getDay(withAny(localDateTime));
                result = 20191111;

                dateUtils.getTime(withAny(localDateTime));
                result = 123456;
            }
        };
    }

}
