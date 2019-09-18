package seed.seedframework.aop;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public interface Pointcut {

    MethodMatcher methodMatch();

}