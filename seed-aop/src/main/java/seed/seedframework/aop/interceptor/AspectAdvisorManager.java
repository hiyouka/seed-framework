package seed.seedframework.aop.interceptor;

import seed.seedframework.aop.adapter.AdvisorAdapterRegister;
import seed.seedframework.aop.adapter.PointcutAdvisor;
import seed.seedframework.aop.matcher.MethodMatcher;
import seed.seedframework.core.intercept.Advisor;
import seed.seedframework.core.intercept.AdvisorManager;
import seed.seedframework.core.intercept.MethodInterceptor;
import seed.seedframework.util.ArrayUtils;
import seed.seedframework.util.Assert;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class AspectAdvisorManager implements AdvisorManager {

    private List<Advisor> advisors = new ArrayList<>();

    private final Class<?> targetClass;

    private Object target;

    private Object[] args;

    private Class[] argTypes;

    private Map<Method,List<MethodInterceptor>> methodCache = new ConcurrentHashMap<>();

    public AspectAdvisorManager(Class<?> targetClass) {
        this.targetClass = targetClass;
    }

    public AspectAdvisorManager(Class<?> targetClass,Advisor[] advisors) {
        this.targetClass = targetClass;
        this.advisors = ArrayUtils.asList(advisors);
    }


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

    public List<MethodInterceptor> getAdvisorInterceptors(Method method){
        List<MethodInterceptor> interceptors = methodCache.get(method);
        if(interceptors == null){
            interceptors = new LinkedList<>();
            AdvisorAdapterRegister register = GlobalAdvisorAdapterRegistry.getInstance();
            for(Advisor advisor : this.advisors){
                if(advisor instanceof PointcutAdvisor){
                    MethodMatcher matcher = ((PointcutAdvisor) advisor).getPointcut().methodMatch();
                    boolean match = matcher.match(method);
                    if(match){
                        MethodInterceptor[] ins = register.getInterceptors(advisor);
                        interceptors.addAll(ArrayUtils.asList(ins));
                    }
                }
                else {
                    // do some thing in other type advisor
                }
            }
        }
        return interceptors;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public Class[] getArgTypes() {
        return argTypes;
    }

    public void setArgTypes(Class[] argTypes) {
        this.argTypes = argTypes;
    }
}