package seed.seedframework.aop.util;

import seed.seedframework.aop.pointcut.Pointcut;
import seed.seedframework.util.AnnotatedElementUtils;

import java.lang.reflect.Method;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class AspectUtil {

    public static boolean isPointCutClass(Class<?> clazz){
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for(Method method : declaredMethods){
            if(AnnotatedElementUtils.isAnnotated(method, Pointcut.class.getName())){
                return true;
            }
        }
        return false;
    }



}