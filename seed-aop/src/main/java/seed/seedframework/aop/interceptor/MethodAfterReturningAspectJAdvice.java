package seed.seedframework.aop.interceptor;

import org.aspectj.lang.annotation.AfterReturning;
import seed.seedframework.aop.util.AspectJUtil;
import seed.seedframework.core.intercept.MethodAfterAdvice;
import seed.seedframework.core.intercept.MethodAfterReturnAdviceInterceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * process after method invoke can get return pointcut
 * {@link AfterReturning}
 * {@link MethodAfterReturnAdviceInterceptor}
 * @author hiyouka
 * @since JDK 1.8
 */
public class MethodAfterReturningAspectJAdvice extends AbstractAspectJAdvice implements MethodAfterAdvice {

    private static Class<? extends Annotation>  annotation = AfterReturning.class;

    public MethodAfterReturningAspectJAdvice(Method aspectJMethod, Object aspectJTarget) {
        super(aspectJMethod, aspectJTarget);
    }

    @Override
    protected AspectJUtil.AspectJMethodType getAspectMethodType() {
        return AspectJUtil.AspectJMethodType.AFTER_RETURNING;
    }

    @Override
    protected Class<? extends Annotation> getAnnotationType() {
        return annotation;
    }

    @Override
    public Object after(Object returnVal, Method method, Object[] args, Object target) {
        return invokeMethodWithArgs(method,returnVal,null);
    }

}