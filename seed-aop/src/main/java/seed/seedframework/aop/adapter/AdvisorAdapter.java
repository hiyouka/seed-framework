package seed.seedframework.aop.adapter;

import seed.seedframework.core.intercept.Advice;
import seed.seedframework.core.intercept.Advisor;
import seed.seedframework.core.intercept.MethodInterceptor;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public interface AdvisorAdapter {

    MethodInterceptor getInterceptor(Advisor advisor);

    boolean supportAdvisor(Advice advice);

}