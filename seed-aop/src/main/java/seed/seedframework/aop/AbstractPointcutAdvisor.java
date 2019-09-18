package seed.seedframework.aop;

import seed.seedframework.aop.adapter.PointcutAdvisor;
import seed.seedframework.core.intercept.Advice;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class AbstractPointcutAdvisor implements PointcutAdvisor {



    @Override
    public Pointcut getPointcut() {
        return null;
    }

    @Override
    public Advice getAdvice() {
        return null;
    }
}