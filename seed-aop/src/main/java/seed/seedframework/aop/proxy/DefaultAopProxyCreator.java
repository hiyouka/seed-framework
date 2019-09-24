package seed.seedframework.aop.proxy;

import seed.seedframework.aop.exception.AopCreateException;
import seed.seedframework.aop.interceptor.AspectAdvisorManager;
import seed.seedframework.util.ClassUtils;

import java.lang.reflect.Proxy;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class DefaultAopProxyCreator implements AopProxyCreator {

    @Override
    public AopProxy createAopProxy(AspectAdvisorManager advisorManager) {
        if(hasNoUserSuppliedProxyInterfaces(advisorManager)){
            Class targetClass = advisorManager.getTargetClass();
            if(targetClass == null){
                throw new AopCreateException("create AopProxy error , because of targetClass is null");
            }
            if(targetClass.isInterface() || Proxy.isProxyClass(targetClass)){
                return new JDKAopProxy(advisorManager);
            }
            else {
                return new CglibAopProxy(advisorManager);
            }
        }
        else {
            return new JDKAopProxy(advisorManager);
        }
    }

    private boolean hasNoUserSuppliedProxyInterfaces(AspectAdvisorManager advisorManager) {
        Class[] ifs = ClassUtils.getAllInterface(advisorManager.getTargetClass());
        return (ifs.length == 0 || (ifs.length == 1 && SeedProxy.class.isAssignableFrom(ifs[0])));
    }

}