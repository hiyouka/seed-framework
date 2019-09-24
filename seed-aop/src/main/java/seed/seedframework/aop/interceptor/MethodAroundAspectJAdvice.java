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


    public MethodAroundAspectJAdvice(Method aspectJMethod, Object aspectJTarget) {
        super(aspectJMethod, aspectJTarget);
    }

    @Override
    protected AspectJUtil.AspectJMethodType getAspectMethodType() {
        return AspectJUtil.AspectJMethodType.AROUND;
    }

    @Override
    public Object around(MethodInvocation invocation) throws Throwable {
        return null;
    }


}