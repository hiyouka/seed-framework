package seed.seedframework.aop.interceptor;

import net.sf.cglib.proxy.MethodProxy;
import seed.seedframework.core.intercept.MethodInterceptor;
import seed.seedframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class CglibInterceptorChainMethodInvocation extends AspectjMethodInvocation{

    private MethodProxy methodProxy;

    public CglibInterceptorChainMethodInvocation(List<MethodInterceptor> chain, Method method, Object target, Object[] args, MethodProxy methodProxy) {
        super(chain, method, target, args);
        this.methodProxy = methodProxy;
    }


    @Override
    protected Object invokeMethod(Method method, Object target, Object... args) throws Throwable {
        if(ClassUtils.isCglibProxyClass(target.getClass())){
            return this.methodProxy.invokeSuper(target,args);
        }
        else {
            return this.methodProxy.invoke(target, args);
        }
    }

}
