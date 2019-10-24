package com.wq.concurrency.test.jmockit.demo.mocked;

import java.util.Locale;

//一个简单的类，能用不同语言打招呼
public class HelloJMockit {

    InnerService innerService;

    // 向JMockit打招呼
    public String sayHello() {
        String str = innerService.describe();
        Locale locale = Locale.getDefault();
        if (locale.equals(Locale.CHINA)) {
            // 在中国，就说中文
            return "Chinese，JMockit! -> " + str;
        } else {
            // 在其它国家，就说英文
            return "Hello，JMockit! -> " + str;
        }
    }
}