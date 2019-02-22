package hiyouka.seedframework.util;

import java.lang.reflect.Method;

/**
 * @author hiyouka
 * Date: 2019/1/28
 * @since JDK 1.8
 */
public class ObjectUtils {

    public static boolean isEmpty(Object... array) {
        return (array == null || array.length == 0);
    }


    public static boolean isToStringMethod(Method method) {
        return (method != null && method.getName().equals("toString") && method.getParameterTypes().length == 0);
    }

    public static boolean isHashCodeMethod(Method method) {
        return (method != null && method.getName().equals("hashCode") && method.getParameterTypes().length == 0);
    }

}