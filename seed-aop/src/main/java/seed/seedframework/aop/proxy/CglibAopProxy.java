package seed.seedframework.aop.proxy;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import seed.seedframework.aop.interceptor.AspectAdvisorManager;
import seed.seedframework.aop.interceptor.CglibInterceptorChainMethodInvocation;
import seed.seedframework.util.ClassUtils;
import seed.seedframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class CglibAopProxy extends AbstractAopProxy {

    private final AspectAdvisorManager advised;

    public CglibAopProxy(AspectAdvisorManager advised) {
        this.advised = advised;
    }

    @Override
    public Object getProxy() {
        Class<?> proxyClass = this.advised.getTargetClass();
        if(ClassUtils.isCglibProxyClass(proxyClass)){
            proxyClass = proxyClass.getSuperclass();
        }
        Enhancer enhancer = new Enhancer();
        Callback[] callBacks = getCallBack();
        Class[] types = new Class[callBacks.length];
        for(int i=0; i<types.length; i++){
            types[i] = callBacks[i].getClass();
        }
        enhancer.setCallbackTypes(types);
        enhancer.setCallbacks(callBacks);
        enhancer.setInterfaces(getInterfaces());
        enhancer.setSuperclass(proxyClass);
        return enhancer.create();
    }

    private Callback[] getCallBack(){
        List<Callback> callbacks = new LinkedList<>();
        callbacks.add(new DynamicInitiatorsInterceptor(this.advised));
        return  callbacks.toArray(new Callback[0]);
    }

    private Class[] getInterfaces(){
        List<Class> interfaces = new LinkedList<>();
        interfaces.add(SeedProxy.class);
        return  interfaces.toArray(new Class[0]);
    }


    public static class DynamicInitiatorsInterceptor implements MethodInterceptor{


        private final AspectAdvisorManager advised;

        public DynamicInitiatorsInterceptor(AspectAdvisorManager advised) {
            this.advised = advised;
        }

        @Override
        public Object intercept(Object target, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
            List<seed.seedframework.core.intercept.MethodInterceptor> interceptors =
                    this.advised.getAdvisorInterceptors(method);
            Object processVal;
            if(CollectionUtils.isEmpty(interceptors)){
                return methodProxy.invokeSuper(target,args);
            }
            else {
                processVal = new CglibInterceptorChainMethodInvocation(interceptors, method, target, args,methodProxy).process();
            }
            return processVal;
        }

    }

}