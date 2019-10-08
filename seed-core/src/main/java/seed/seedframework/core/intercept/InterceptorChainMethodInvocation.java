package seed.seedframework.core.intercept;

import seed.seedframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class InterceptorChainMethodInvocation implements MethodInvocation {

    protected List<MethodInterceptor> chain = new CopyOnWriteArrayList<>();

    protected final Method method;

    protected final Object target;

    protected Object[] args;

    protected volatile int currentIndex = 0;

    public InterceptorChainMethodInvocation(Method method, Object target, Object[] args) {
        this.method = method;
        this.target = target;
        this.args = args;
    }

    public InterceptorChainMethodInvocation(List<MethodInterceptor> chain, Method method, Object target, Object[] args) {
        this.chain = chain;
        this.method = method;
        this.target = target;
        this.args = args;
    }

    public void pushInterceptor(MethodInterceptor interceptor){
        this.chain.add(interceptor);
    }

    public void setChain(List<MethodInterceptor> interceptors){
        this.chain.addAll(interceptors);
    }

    @Override
    public Method getMethod() {
        return this.method;
    }

    @Override
    public Object[] getArguments() {
        return this.args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    @Override
    public Object getThis() {
        return this.target;
    }

    @Override
    public Object process() throws Throwable {

        // if no interceptor or end
        if(this.currentIndex == (this.chain.size())){
            return invokeMethod(this.method,this.target,this.args);
        }
        MethodInterceptor currentInterceptor;
        synchronized (this){
            currentInterceptor = chain.get(currentIndex++);
        }
        if(currentInterceptor != null && methodMatch(this.method,this.args)){
            return currentInterceptor.invoke(this);
        }
        // skip
        else {
            return process();
        }

    }

    protected boolean methodMatch(Method method, Object[] args){
        return true;
    }

    protected Object invokeMethod(Method method, Object target, Object... args) throws Throwable {
        return ReflectionUtils.invokeMethod(method,target,args);
    }


}