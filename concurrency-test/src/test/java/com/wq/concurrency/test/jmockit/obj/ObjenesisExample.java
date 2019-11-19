package com.wq.concurrency.test.jmockit.obj;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

/**
 * @Author: wangqiang20995
 * @Date: 2019/11/4 13:26
 * @Description:
 **/
public class ObjenesisExample {

    public static void main(String[] args){
        Objenesis objenesis = new ObjenesisStd();
        EmailCode expected = objenesis.newInstance(EmailCode.class);
        expected.setCode("123");
        expected.setEmail("wangqiang@qq.com");
        EmailCode actual = new EmailCode();
        actual.setCode("123");
        actual.setEmail("wangqiang@qq.com");

        System.out.println(actual.equals(expected));
        System.out.println(expected.equals(actual));
        System.out.println(mergeDateTime(20191113,190034));
    }
    private static long mergeDateTime(Integer date,Integer time){
        if (time == null){
            time = 0;
        }
        return date.longValue() * 1000000L + time.longValue();
    }
}
