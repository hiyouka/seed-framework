package seed.seedframework.aop.proxy;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import seed.seedframework.aop.interceptor.AspectAdvisorManager;

import java.lang.reflect.Method;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class CglibAopProxy implements AopProxy {

    @Override
    public Object getProxy() {
        return null;
    }

    public static class DynamicInitiatorsInterceptor implements MethodInterceptor{

        private AspectAdvisorManager advised;

        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {

            return null;
        }

    }

}