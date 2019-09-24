package seed.seedframework.aop.proxy;

import seed.seedframework.aop.interceptor.AspectAdvisorManager;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public interface AopProxyCreator {

    AopProxy createAopProxy(AspectAdvisorManager advisorManager);

}