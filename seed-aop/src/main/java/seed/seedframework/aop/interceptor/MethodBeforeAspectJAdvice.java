package seed.seedframework.aop.interceptor;

import org.aspectj.lang.annotation.Before;
import seed.seedframework.aop.util.AspectJUtil;
import seed.seedframework.core.intercept.MethodBeforeAdvice;
import seed.seedframework.core.intercept.MethodBeforeAdviceInterceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * process before method pointcut
 * {@link Before}
 * {@link MethodBeforeAdviceInterceptor}
 * @author hiyouka
 * @since JDK 1.8
 */
public class MethodBeforeAspectJAdvice extends AbstractAspectJAdvice implements MethodBeforeAdvice {

    private static Class<? extends Annotation>  annotation = Before.class;

    public MethodBeforeAspectJAdvice(Method aspectJMethod, Object aspectJTarget) {
        super(aspectJMethod, aspectJTarget);
    }

    @Override
    protected AspectJUtil.AspectJMethodType getAspectMethodType() {
        return AspectJUtil.AspectJMethodType.BEFORE;
    }

    @Override
    protected Class<? extends Annotation> getAnnotationType() {
        return annotation;
    }

    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        invokeMethodWithArgs(method,null,null);
    }
}