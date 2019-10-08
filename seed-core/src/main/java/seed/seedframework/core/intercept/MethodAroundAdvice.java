package seed.seedframework.core.intercept;

import java.lang.reflect.Method;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public interface MethodAroundAdvice extends AroundAdvice{

    Object around(Method method, Object[] args, Object target) throws Throwable;



}