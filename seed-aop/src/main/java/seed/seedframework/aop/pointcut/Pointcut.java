package seed.seedframework.aop.pointcut;

import seed.seedframework.aop.matcher.MethodMatcher;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public interface Pointcut {

    MethodMatcher methodMatch();

}