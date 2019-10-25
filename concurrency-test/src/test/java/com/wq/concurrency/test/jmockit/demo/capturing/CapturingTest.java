package com.wq.concurrency.test.jmockit.demo.capturing;

import mockit.Capturing;
import mockit.Expectations;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CapturingTest {
    private static long UID = 123;

    private static class SubPrivilege implements Privilege{
        @Override
        public boolean allow(long id) {
            return id != CapturingTest.UID;
        }
    }

    private Privilege privilege1 = new SubPrivilege();

    private Privilege privilege2 = (Privilege) Proxy.newProxyInstance(CapturingTest.class.getClassLoader(), new Class[]{Privilege.class},
            new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    long id = (long) args[0];
                    if(id == 123){
                        return false;
                    }
                    return true;
                }
            });


    @Test
    public void testWithCapturing(@Capturing Privilege privilege){
        // 测试子类的mock
        new Expectations(){
            {
                privilege.allow(UID);
                result = true;
            }
        };

        Assert.assertTrue(privilege1.allow(UID));
        Assert.assertTrue(privilege2.allow(UID));
    }


    @Test
    public void testWithoutCapturing(){
        // 不使用@Capturing修饰的mock对象，不record
        Assert.assertTrue(!privilege1.allow(UID));
        Assert.assertTrue(!privilege2.allow(UID));
    }

    @Test
    public void toArray(){
        List<String> list = new ArrayList<>();
        list.add("hello");
        list.add("world");
        String[] ary = list.toArray(new String[]{});
        Arrays.stream(ary).forEach(item -> System.out.println(item));
    }

}
