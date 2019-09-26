package aspect;

import org.aspectj.lang.annotation.Pointcut;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class PointcutTest {

    @Pointcut(value = "execution(* seed.seedframework.aop..*(..)) || @annotation(aspect.aop.AopBefore)")
    public void point(){}

}