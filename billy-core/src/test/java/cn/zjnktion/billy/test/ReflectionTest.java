package cn.zjnktion.billy.test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengjn on 2016/4/27.
 */
public class ReflectionTest {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        List<String> list = new ArrayList<String>();

        Class clazz = list.getClass();

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            System.out.println(field.getName());
        }

        Field field = clazz.getDeclaredField("MAX_ARRAY_SIZE");
        field.setAccessible(true);
        System.out.println(field.get(list));
    }
}
