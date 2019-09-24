package seed.seedframework.aop.interceptor;

import seed.seedframework.aop.util.AspectJUtil;
import seed.seedframework.core.intercept.MethodBeforeAdvice;

import java.lang.reflect.Method;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class MethodBeforeAspectJAdvice extends AbstractAspectJAdvice implements MethodBeforeAdvice {


    public MethodBeforeAspectJAdvice(Method aspectJMethod, Object aspectJTarget) {
        super(aspectJMethod, aspectJTarget);
    }

    @Override
    protected AspectJUtil.AspectJMethodType getAspectMethodType() {
        return AspectJUtil.AspectJMethodType.BEFORE;
    }

    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        invokeMethodWithArgs(null,method,args,target);
    }
}