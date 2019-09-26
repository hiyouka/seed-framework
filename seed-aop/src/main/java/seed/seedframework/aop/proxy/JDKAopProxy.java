package seed.seedframework.aop.proxy;

import seed.seedframework.aop.exception.AopCreateException;
import seed.seedframework.aop.interceptor.AspectAdvisorManager;
import seed.seedframework.core.intercept.InterceptorChainMethodInvocation;
import seed.seedframework.core.intercept.MethodInterceptor;
import seed.seedframework.util.CollectionUtils;
import seed.seedframework.util.ReflectionUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * create proxy object use jdk dynamic
 * @author hiyouka
 * @since JDK 1.8
 */
public class JDKAopProxy extends AbstractAopProxy {

    private final AspectAdvisorManager advised;

    public JDKAopProxy(AspectAdvisorManager advised) {
        this.advised = advised;
    }

    @Override
    public Object getProxy() {
        Class<?> targetClass = this.advised.getTargetClass();
        return Proxy.newProxyInstance(targetClass.getClassLoader(),targetClass.getInterfaces(),
                new DynamicInitiatorsInvocationHandler(advised));
    }

    public static class DynamicInitiatorsInvocationHandler implements InvocationHandler {

        private final AspectAdvisorManager advised;

        public DynamicInitiatorsInvocationHandler(AspectAdvisorManager advised) {
            this.advised = advised;
        }

        @Override
        public Object invoke(Object target, Method method, Object[] args) throws Throwable {

            List<MethodInterceptor> interceptors =
                    this.advised.getAdvisorInterceptors(getImplMethod(method,this.advised.getTargetClass()));
            Object targetSource = target;
            Object source = this.advised.getTarget();
            if(source != null){
                targetSource = source;
            }
            Object processVal;
            if(CollectionUtils.isEmpty(interceptors)){
//                Object originBean = BeanUtils.instanceClass(this.advised.getTargetClass());
                return ReflectionUtils.invokeMethod(method,targetSource,args);
            }
            else {
                processVal = new InterceptorChainMethodInvocation(interceptors,method,targetSource,args){
                    @Override
                    protected Object invokeMethod(Method method, Object target, Object... args) {
//                        Object originBean = BeanUtils.instanceClass(advised.getTargetClass());
                        return ReflectionUtils.invokeMethod(method,this.target,args);
                    }
                }.process();
            }
            return processVal;
        }

    }

    /**
     * get impl class method override interface method
     * @param superMethod super interface abstract method
     * @param targetClass real class type
     * @return java.lang.reflect.Method
     * @throws
     */
    private static Method getImplMethod(Method superMethod,Class<?> targetClass) {
        try {
            return targetClass.getMethod(superMethod.getName(), superMethod.getParameterTypes());
        } catch (NoSuchMethodException e) {
            throw new AopCreateException("no such impl class method : " + superMethod.getName());
        }
    }

}