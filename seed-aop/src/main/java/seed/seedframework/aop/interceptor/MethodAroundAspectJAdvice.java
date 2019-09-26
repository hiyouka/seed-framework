package seed.seedframework.aop.interceptor;

import org.aspectj.lang.annotation.Around;
import seed.seedframework.aop.util.AspectJUtil;
import seed.seedframework.core.intercept.MethodAroundAdvice;
import seed.seedframework.core.intercept.MethodAroundAdviceInterceptor;
import seed.seedframework.core.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 * process around method pointcut
 * {@link Around}
 * {@link MethodAroundAdviceInterceptor}
 * @author hiyouka
 * @since JDK 1.8
 */
public class MethodAroundAspectJAdvice extends AbstractAspectJAdvice implements MethodAroundAdvice {


    public MethodAroundAspectJAdvice(Method aspectJMethod, Object aspectJTarget) {
        super(aspectJMethod, aspectJTarget);
    }

    @Override
    protected AspectJUtil.AspectJMethodType getAspectMethodType() {
        return AspectJUtil.AspectJMethodType.AROUND;
    }

    @Override
    public Object around(MethodInvocation invocation) throws Throwable {
        return invokeWithInvocation(invocation);
    }



}