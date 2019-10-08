package seed.seedframework.aop.interceptor;

import org.aspectj.lang.annotation.After;
import seed.seedframework.aop.util.AspectJUtil;
import seed.seedframework.core.intercept.MethodAfterAdvice;
import seed.seedframework.core.intercept.MethodAfterReturnAdviceInterceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * process after method invoke pointcut
 * {@link After}
 * {@link MethodAfterReturnAdviceInterceptor}
 * @author hiyouka
 * @since JDK 1.8
 */
public class MethodAfterAspectJAdvice extends AbstractAspectJAdvice implements MethodAfterAdvice {

    private static Class<? extends Annotation>  annotation = After.class;

    public MethodAfterAspectJAdvice(Method aspectJMethod, Object aspectJTarget) {
        super(aspectJMethod, aspectJTarget);
    }

    @Override
    protected AspectJUtil.AspectJMethodType getAspectMethodType() {
        return AspectJUtil.AspectJMethodType.AFTER;
    }

    @Override
    protected Class<? extends Annotation> getAnnotationType() {
        return annotation;
    }

    @Override
    public Object after(Object returnVal, Method method, Object[] args, Object target) {
        invokeMethodWithArgs(method,null,null);
        return returnVal;
    }
}