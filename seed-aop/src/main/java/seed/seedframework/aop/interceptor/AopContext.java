package seed.seedframework.aop.interceptor;

import seed.seedframework.core.intercept.MethodInvocation;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class AopContext {

    private static final ThreadLocal<MethodInvocation> currentInvocation = new ThreadLocal<>();

    public static MethodInvocation currentMethodInvocation(){
        return currentInvocation.get();
    }

    public static void setCurrentMethodInvocation(MethodInvocation invocation){
            currentInvocation.set(invocation);
    }

}