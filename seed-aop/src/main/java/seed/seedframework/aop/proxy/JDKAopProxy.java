package seed.seedframework.aop.proxy;

import seed.seedframework.aop.interceptor.AspectAdvisorManager;
import seed.seedframework.core.intercept.InterceptorChainMethodInvocation;
import seed.seedframework.core.intercept.MethodInterceptor;
import seed.seedframework.util.BeanUtils;
import seed.seedframework.util.CollectionUtils;
import seed.seedframework.util.ReflectionUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
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
        return Proxy.newProxyInstance(targetClass.getClassLoader(),targetClass.getInterfaces(),new DynamicInitiatorsInvocationHandler(advised));
    }

    public static class DynamicInitiatorsInvocationHandler implements InvocationHandler {

        private final AspectAdvisorManager advised;

        public DynamicInitiatorsInvocationHandler(AspectAdvisorManager advised) {
            this.advised = advised;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            List<MethodInterceptor> interceptors =
                    this.advised.getAdvisorInterceptors(method);
            Object processVal;
            if(CollectionUtils.isEmpty(interceptors)){
                BeanUtils.instanceClass(this.advised.getTargetClass());
                return ReflectionUtils.invokeMethod(method,proxy,args);
            }
            else {
                processVal = new InterceptorChainMethodInvocation(method,proxy,args).process();
            }
            return processVal;
        }

    }

}