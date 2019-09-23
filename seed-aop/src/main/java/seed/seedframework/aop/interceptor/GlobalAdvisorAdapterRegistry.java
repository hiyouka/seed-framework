package seed.seedframework.aop.interceptor;

import seed.seedframework.aop.adapter.AdvisorAdapterRegister;
import seed.seedframework.aop.adapter.AopAdvisorAdapterRegister;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class GlobalAdvisorAdapterRegistry {

    private static AdvisorAdapterRegister instance = new AopAdvisorAdapterRegister();

    public static AdvisorAdapterRegister getInstance() {
        return instance;
    }

}