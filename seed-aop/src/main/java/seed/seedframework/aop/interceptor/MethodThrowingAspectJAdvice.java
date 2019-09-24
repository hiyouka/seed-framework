package seed.seedframework.aop.interceptor;

import seed.seedframework.aop.util.AspectJUtil;
import seed.seedframework.core.intercept.ThrowsAdvice;

import java.lang.reflect.Method;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class MethodThrowingAspectJAdvice extends AbstractAspectJAdvice implements ThrowsAdvice {

    public MethodThrowingAspectJAdvice(Method aspectJMethod, Object aspectJTarget) {
        super(aspectJMethod, aspectJTarget);
    }

    @Override
    protected AspectJUtil.AspectJMethodType getAspectMethodType() {
        return AspectJUtil.AspectJMethodType.AFTER_THROWING;
    }

    @Override
    public void doOfThrow(Method method, Object[] arguments, Object aThis) throws Throwable {
        
    }

}