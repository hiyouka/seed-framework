package seed.seedframework.core.intercept;

import seed.seedframework.util.Assert;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class MethodAfterReturnAdviceInterceptor implements MethodInterceptor{

    private final MethodAfterAdvice advice;

    public MethodAfterReturnAdviceInterceptor(MethodAfterAdvice advice) {
        Assert.notNull(advice,"advice must not ull");
        this.advice = advice;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object returnVal = invocation.process();
        return this.advice.after(returnVal,invocation.getMethod(),invocation.getArguments(),invocation.getThis());
    }


}