package com.wq.concurrency.test.jmockit.demo.mockup;

import junit.framework.Assert;
import mockit.Mock;
import mockit.MockUp;
import org.junit.Test;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MockUpTest {

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
}
