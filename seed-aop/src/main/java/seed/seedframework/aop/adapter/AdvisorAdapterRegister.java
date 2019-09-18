package seed.seedframework.aop.adapter;

import seed.seedframework.core.intercept.Advisor;
import seed.seedframework.core.intercept.MethodInterceptor;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public interface AdvisorAdapterRegister {

    void addAdapter(AdvisorAdapter adapter);

    MethodInterceptor[] getInterceptors(Advisor advisor);

}