package seed.seedframework.aop.pointcut;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public interface ClassPointcut extends Pointcut {

    Class<?> getThis();

}