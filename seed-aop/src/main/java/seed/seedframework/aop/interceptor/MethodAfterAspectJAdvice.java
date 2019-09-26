package seed.seedframework.aop.interceptor;

import org.aspectj.lang.annotation.After;
import seed.seedframework.aop.util.AspectJUtil;
import seed.seedframework.core.intercept.MethodAfterAdvice;
import seed.seedframework.core.intercept.MethodAfterReturnAdviceInterceptor;

import java.lang.reflect.Method;

/**
 * process after method invoke pointcut
 * {@link After}
 * {@link MethodAfterReturnAdviceInterceptor}
 * @author hiyouka
 * @since JDK 1.8
 */
public class MethodAfterAspectJAdvice extends AbstractAspectJAdvice implements MethodAfterAdvice {


    public MethodAfterAspectJAdvice(Method aspectJMethod, Object aspectJTarget) {
        super(aspectJMethod, aspectJTarget);
    }

    @Override
    protected AspectJUtil.AspectJMethodType getAspectMethodType() {
        return AspectJUtil.AspectJMethodType.AFTER;
    }

    @Override
    public Object after(Object returnVal, Method method, Object[] args, Object target) {
        invokeMethodWithArgs(returnVal,method,args,target);
        return returnVal;
    }
}