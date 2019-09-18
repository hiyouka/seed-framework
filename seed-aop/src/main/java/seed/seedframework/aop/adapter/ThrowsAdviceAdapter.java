package seed.seedframework.aop.adapter;

import seed.seedframework.core.intercept.*;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class ThrowsAdviceAdapter implements AdvisorAdapter {

    @Override
    public MethodInterceptor getInterceptor(Advisor advisor) {
        return new ThrowsAdviceInterceptor((ThrowsAdvice) advisor.getAdvice());
    }

    @Override
    public boolean supportAdvisor(Advice advice) {
        return advice instanceof ThrowsAdvice;
    }

}