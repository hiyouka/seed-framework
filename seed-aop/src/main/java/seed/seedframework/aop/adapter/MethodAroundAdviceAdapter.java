package seed.seedframework.aop.adapter;

import seed.seedframework.core.intercept.*;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class MethodAroundAdviceAdapter implements AdvisorAdapter {

    @Override
    public MethodInterceptor getInterceptor(Advisor advisor) {
        return new MethodAroundAdviceInterceptor((MethodAroundAdvice) advisor.getAdvice());
    }

    @Override
    public boolean supportAdvisor(Advice advice) {
        return advice instanceof MethodAroundAdvice;
    }

}