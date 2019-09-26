package seed.seedframework.aop.interceptor;

import org.aspectj.lang.ProceedingJoinPoint;
import seed.seedframework.aop.pointcut.AspectPointcut;
import seed.seedframework.aop.util.AspectJUtil;
import seed.seedframework.core.intercept.Advice;
import seed.seedframework.core.intercept.MethodInterceptor;
import seed.seedframework.core.intercept.MethodInvocation;
import seed.seedframework.util.Assert;
import seed.seedframework.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * {@link MethodInterceptor}
 * @author hiyouka
 * @since JDK 1.8
 */
public abstract class AbstractAspectJAdvice implements Advice {

    private Method aspectJMethod;

    private String methodName;

    private AspectPointcut aspectPointcut;

    private Class targetClass;

    private Object aspectJTarget;

    public AbstractAspectJAdvice(Method aspectJMethod,Object aspectJTarget) {
        Assert.state(AspectJUtil.isAspectJMethod(aspectJMethod),"not support be not aspectJ method !!");
        this.aspectJMethod = aspectJMethod;
        this.methodName = aspectJMethod.getName();
        this.targetClass = aspectJMethod.getDeclaringClass();
        this.aspectPointcut = buildAspectPointcut();
        this.aspectJTarget = aspectJTarget;
    }

    protected AspectPointcut buildAspectPointcut(){
        AspectJUtil.AspectJMethodType aspectMethodType = getAspectMethodType();
        return new AspectPointcut(
                AspectJUtil.getAspectJMethodExpression(aspectMethodType,this.aspectJMethod),null);
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

    protected Object invokeMethodWithArgs(Object returnVal, Method method, Object[] args, Object target){
        ReflectionUtils.invokeMethod(this.aspectJMethod,this.aspectJTarget);
        return null;
    }


    protected Object invokeWithInvocation(MethodInvocation invocation){

        Class<?>[] parameterTypes = this.aspectJMethod.getParameterTypes();
        Object[] args = new Object[parameterTypes.length];
        for(int i=0; i<args.length; i++){
            if(parameterTypes[i].equals(ProceedingJoinPoint.class)){
                args[i] = invocation;
            }
            else {
                args[i] = null;
            }

        }
        return ReflectionUtils.invokeMethod(this.aspectJMethod,this.aspectJTarget,args);

    }
}