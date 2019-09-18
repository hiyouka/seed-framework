package seed.seedframework.aop.adapter;

import seed.seedframework.core.intercept.*;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class MethodBeforeAdviceAdapter implements AdvisorAdapter{

    @Override
    public MethodInterceptor getInterceptor(Advisor advisor) {
        return new MethodBeforeAdviceInterceptor((MethodBeforeAdvice) advisor.getAdvice());
    }

    @Override
    public boolean supportAdvisor(Advice advice) {
        return advice instanceof MethodBeforeAdvice;
    }

}