package seed.seedframework.aop.adapter;

import seed.seedframework.aop.Pointcut;
import seed.seedframework.core.intercept.Advisor;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public interface PointcutAdvisor extends Advisor {

    Pointcut getPointcut();

}