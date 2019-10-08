package seed.seedframework.aop.interceptor;

import org.aspectj.lang.annotation.Around;
import seed.seedframework.aop.util.AspectJUtil;
import seed.seedframework.core.intercept.MethodAroundAdvice;
import seed.seedframework.core.intercept.MethodAroundAdviceInterceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * process around method pointcut
 * {@link Around}
 * {@link MethodAroundAdviceInterceptor}
 * @author hiyouka
 * @since JDK 1.8
 */
public class MethodAroundAspectJAdvice extends AbstractAspectJAdvice implements MethodAroundAdvice {

    private static Class<? extends Annotation>  annotation = Around.class;

    public MethodAroundAspectJAdvice(Method aspectJMethod, Object aspectJTarget) {
        super(aspectJMethod, aspectJTarget);
    }

    @Override
    protected AspectJUtil.AspectJMethodType getAspectMethodType() {
        return AspectJUtil.AspectJMethodType.AROUND;
    }

    @Override
    protected Class<? extends Annotation> getAnnotationType() {
        return annotation;
    }

    @Override
    public Object around(Method method, Object[] args, Object target) throws Throwable {
        return invokeMethodWithArgs(method,null,null);
    }

}