package seed.seedframework.aop.interceptor;

import seed.seedframework.aop.adapter.AdvisorAdapterRegister;
import seed.seedframework.core.intercept.Advisor;
import seed.seedframework.core.intercept.AdvisorManager;
import seed.seedframework.core.intercept.Interceptor;
import seed.seedframework.core.intercept.MethodInterceptor;
import seed.seedframework.util.ArrayUtils;
import seed.seedframework.util.Assert;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class AspectAdvisorManager implements AdvisorManager {

    private List<Advisor> advisors = new ArrayList<>();

    private Map<Method,List<Interceptor>> methodCache = new ConcurrentHashMap<>();

    @Override
    public Advisor[] getAdvisors() {
        return this.advisors.toArray(new Advisor[0]);
    }

    public void addAdvisor(Advisor advisor){
        Assert.notNull(advisor,"advisor must not null");
        this.advisors.add(advisor);
    }

    public void removeAdvisor(Advisor advisor){
        Assert.notNull(advisor,"advisor must not null");
        this.advisors.remove(advisor);
    }

    public List<Interceptor> getAdvisorInterceptors(Method method){
        List<Interceptor> interceptors = methodCache.get(method);
        if(interceptors == null){
            AdvisorAdapterRegister register = GlobalAdvisorAdapterRegistry.getInstance();
            for(Advisor advisor : this.advisors){
                MethodInterceptor[] ins = register.getInterceptors(advisor);
                interceptors.addAll(ArrayUtils.asList(ins));
            }
        }
        return interceptors;
    }



}