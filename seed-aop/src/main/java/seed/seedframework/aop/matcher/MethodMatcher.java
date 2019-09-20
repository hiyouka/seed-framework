package seed.seedframework.aop.matcher;

import java.lang.reflect.Method;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public interface MethodMatcher {

    boolean match(Method method);

}