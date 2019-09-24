package seed.seedframework.aop.interceptor;

import seed.seedframework.aop.util.AspectJUtil;
import seed.seedframework.core.intercept.MethodAfterAdvice;

import java.lang.reflect.Method;

/**
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