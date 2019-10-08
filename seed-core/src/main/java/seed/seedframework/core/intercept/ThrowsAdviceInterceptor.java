package seed.seedframework.core.intercept;

import seed.seedframework.util.Assert;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class ThrowsAdviceInterceptor implements MethodInterceptor {

    private final ThrowsAdvice advice;

    public ThrowsAdviceInterceptor(ThrowsAdvice advice) {
        Assert.notNull(advice,"advice must not null");
        this.advice = advice;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            return invocation.process();
        }
        catch (Throwable ex){
            this.advice.doOfThrow(invocation.getMethod(),invocation.getArguments(),invocation.getThis(),ex);
            throw ex;
        }
    }
}