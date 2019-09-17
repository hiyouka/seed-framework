package seed.seedframework.core.intercept;

import java.lang.reflect.Method;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public interface MethodBeforeAdvice extends BeforeAdvice{

    void before(Method method,Object[] args, Object target) throws Throwable;

}