package seed.seedframework.aop.interceptor;

import seed.seedframework.aop.util.AspectJUtil;
import seed.seedframework.core.intercept.MethodAroundAdvice;
import seed.seedframework.core.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class MethodAroundAspectJAdvice extends AbstractAspectJAdvice implements MethodAroundAdvice {

    public MethodAroundAspectJAdvice(Method aspectJMethod) {
        super(aspectJMethod);
    }

    @Override
    protected AspectJUtil.AspectJMethodType getAspectMethodType() {
        return null;
    }

    @Override
    public Object around(MethodInvocation invocation) throws Throwable {
        return null;
    }


}