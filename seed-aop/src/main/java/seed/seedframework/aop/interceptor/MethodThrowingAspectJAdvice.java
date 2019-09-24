package seed.seedframework.aop.interceptor;

import seed.seedframework.aop.util.AspectJUtil;
import seed.seedframework.core.intercept.ThrowsAdvice;

import java.lang.reflect.Method;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class MethodThrowingAspectJAdvice extends AbstractAspectJAdvice implements ThrowsAdvice {

    public MethodThrowingAspectJAdvice(Method aspectJMethod) {
        super(aspectJMethod);
    }

    @Override
    protected AspectJUtil.AspectJMethodType getAspectMethodType() {
        return null;
    }

    @Override
    public void doOfThrow(Method method, Object[] arguments, Object aThis) throws Throwable {

    }

}