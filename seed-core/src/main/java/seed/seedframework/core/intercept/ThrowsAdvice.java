package seed.seedframework.core.intercept;

import java.lang.reflect.Method;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public interface ThrowsAdvice extends Advice {

    void doOfThrow(Method method, Object[] arguments, Object aThis) throws Throwable;

}