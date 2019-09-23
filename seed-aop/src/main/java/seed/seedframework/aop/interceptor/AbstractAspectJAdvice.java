package seed.seedframework.aop.interceptor;

import seed.seedframework.aop.pointcut.AspectPointcut;
import seed.seedframework.aop.util.AspectJUtil;
import seed.seedframework.core.intercept.Advice;
import seed.seedframework.util.Assert;

import java.lang.reflect.Method;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public abstract class AbstractAspectJAdvice implements Advice {

    private Method aspectJMethod;

    private String methodName;

    private AspectPointcut aspectPointcut;

    private Class targetClass;

    public AbstractAspectJAdvice(Method aspectJMethod) {
        Assert.state(AspectJUtil.isAspectJMethod(aspectJMethod),"not support be not aspectJ method !!");
        this.aspectJMethod = aspectJMethod;
        this.methodName = aspectJMethod.getName();
        this.targetClass = aspectJMethod.getDeclaringClass();
        this.aspectPointcut = buildAspectPointcut();
    }

    protected AspectPointcut buildAspectPointcut(){
        AspectJUtil.AspectJMethodType aspectMethodType = getAspectMethodType();
        return new AspectPointcut(
                AspectJUtil.getAspectJMethodExpression(aspectMethodType,this.aspectJMethod),this.targetClass);
    }

    protected abstract AspectJUtil.AspectJMethodType getAspectMethodType();

    protected Method getAspectJMethod() {
        return this.aspectJMethod;
    }

    protected String getMethodName() {
        return this.methodName;
    }

    public AspectPointcut getAspectPointcut() {
        return aspectPointcut;
    }

    public Class getTargetClass() {
        return targetClass;
    }

}