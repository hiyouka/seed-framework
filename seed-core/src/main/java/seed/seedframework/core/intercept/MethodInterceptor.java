package seed.seedframework.core.intercept;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
@FunctionalInterface
public interface MethodInterceptor extends Interceptor{

    Object invoke(MethodInvocation invocation) throws Throwable;

}