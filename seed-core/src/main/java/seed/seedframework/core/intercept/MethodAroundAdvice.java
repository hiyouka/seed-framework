package seed.seedframework.core.intercept;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public interface MethodAroundAdvice extends AroundAdvice{

    default Object around(MethodInvocation invocation) throws Throwable{
        return invocation.process();
    }



}