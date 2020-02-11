import sun.misc.Unsafe;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @Author: wangqiang20995
 * @Date: 2020/2/11 17:31
 * @Description: 同一个类加载器加载一个类文件多次并且得到多个class对象而都可以被java层使用
 **/
public class JvmClassLoaderTwice {

    public static void main(String[] args) throws Throwable {
        Field field = Unsafe.class.getDeclaredField("theUnsafe");
        field.setAccessible(true);

        Unsafe unsafe = (Unsafe) field.get(null);
        String filePath = "InnerClass.class";
        InputStream inputStream = JvmClassLoaderTwice.class.getClassLoader().getResourceAsStream(filePath);
        byte[] buffer = new byte[inputStream.available()];
        inputStream.read(buffer);
        inputStream.close();

        Class<?> c1 = unsafe.defineAnonymousClass(JvmClassLoaderTwice.class,buffer,null);
        Class<?> c2 = unsafe.defineAnonymousClass(JvmClassLoaderTwice.class,buffer,null);

        System.out.println(c1 == c2);
        System.out.println(c1);
        System.out.println(c2);

        Constructor<?> con1 = c1.getDeclaredConstructor(new Class[]{});
        Constructor<?> con2 = c2.getDeclaredConstructor(new Class[]{});

        // 输出实例所属class的地址
        Object o1 = con1.newInstance(null);
        Object o2 = con2.newInstance(null);

        System.out.println(o1.getClass());
        System.out.println(o2.getClass());
        System.out.println(o1.getClass() == o2.getClass());

        // 反射执行方法
        Method m1 = c1.getDeclaredMethod("display",null);
        Method m2 = c2.getDeclaredMethod("display",null);

        m1.invoke(o1,null);
        m2.invoke(o2,null);
    }
}
