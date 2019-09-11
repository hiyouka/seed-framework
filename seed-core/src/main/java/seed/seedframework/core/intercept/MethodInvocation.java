package seed.seedframework.core.intercept;

import java.lang.reflect.Method;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public interface MethodInvocation extends Invocation {

    Method getMethod();

}