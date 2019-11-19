package com.wq.concurrency.test.jmockit.demo.mockup;

import junit.framework.Assert;
import mockit.*;
import org.junit.Test;

import java.util.Calendar;

public class MockUpTest {

    private DatetimeWrapper datetimeWrapper;

    @Test
    public void test() {
        new MockUp<Calendar>(Calendar.class) {
            @Mock
            public int get(int uint) {
                if (uint == Calendar.YEAR) {
                    return 2017;
                }
                return 0;
            }
        };
        Calendar calendar = Calendar.getInstance();
        Assert.assertEquals(2017,calendar.get(Calendar.YEAR));
    }

    @Test
    public void privateMethod(){
        new MockUp<DatetimeWrapper>(){
            @Mock
            private String display(){
                return "abc";
            }
        };

//        new Expectations(){
//            {
//                datetimeWrapper.getDisplayLength();
//                result = 1;
//            }
//        };

        DatetimeWrapper datetimeWrapper = DatetimeWrapper.newInstance();
        int length = datetimeWrapper.getDisplayLength();
        Assert.assertEquals(3,length);

    }
}
