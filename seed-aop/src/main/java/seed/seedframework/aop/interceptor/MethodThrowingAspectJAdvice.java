package seed.seedframework.aop.interceptor;

import org.aspectj.lang.annotation.AfterThrowing;
import seed.seedframework.aop.util.AspectJUtil;
import seed.seedframework.core.intercept.ThrowsAdvice;
import seed.seedframework.core.intercept.ThrowsAdviceInterceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * process after method throw exception pointcut
 * {@link AfterThrowing}
 * {@link ThrowsAdviceInterceptor}
 * @author hiyouka
 * @since JDK 1.8
 */
public class MethodThrowingAspectJAdvice extends AbstractAspectJAdvice implements ThrowsAdvice {

    private static Class<? extends Annotation>  annotation = AfterThrowing.class;

    public MethodThrowingAspectJAdvice(Method aspectJMethod, Object aspectJTarget) {
        super(aspectJMethod, aspectJTarget);
    }

    @Override
    protected AspectJUtil.AspectJMethodType getAspectMethodType() {
        return AspectJUtil.AspectJMethodType.AFTER_THROWING;
    }

    @Override
    protected Class<? extends Annotation> getAnnotationType() {
        return annotation;
    }

    @Override
    public void doOfThrow(Method method, Object[] arguments, Object aThis,Throwable ex) throws Throwable {
        invokeMethodWithArgs(method,null,ex);
    }

}