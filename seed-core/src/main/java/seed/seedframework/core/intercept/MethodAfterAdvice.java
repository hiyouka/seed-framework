package seed.seedframework.core.intercept;

import java.lang.reflect.Method;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public interface MethodAfterAdvice extends AfterAdvice{

    Object after(Object returnVal, Method method, Object[] args, Object target);

}