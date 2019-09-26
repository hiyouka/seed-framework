package aspect.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import seed.seedframework.beans.annotation.Component;
import seed.seedframework.core.annotation.Order;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
@Aspect
@Component
public class PointTest {

    @Pointcut(value = "execution(* seed.seedframework.aop..*(..))")
    private void point(){};


    @Order(5)
    @Before("aspect.PointcutTest.point() || @annotation(aspect.aop.AopBefore)")
    public void before(){
        System.out.println(">>>>>>>>>>>>>>>>before ");
    }

    @Order(4)
    @AfterThrowing("aspect.PointcutTest.point() || @annotation(aspect.aop.AopBefore)" )
    public void afterThrow(){
        System.out.println(">>>>>>>>>>>>>afterThrow");
    }

    @Order(3)
    @After("aspect.PointcutTest.point() || @annotation(aspect.aop.AopBefore)")
    public void after(){
        System.out.println(">>>>>>>>>>>>>>>after");
    }

    @Order(2)
    @AfterReturning("aspect.PointcutTest.point() || @annotation(aspect.aop.AopBefore)")
    public void afterReturn(){
        System.out.println(">>>>>>>>>>>>>after Return");
    }

    @Order(1)
    @Around(value = "aspect.PointcutTest.point() || @annotation(aspect.aop.AopBefore)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println(">>>>>>>>>>>>>>around before");
        Object process = joinPoint.proceed();
        System.out.println(">>>>>>>>>>>>>>>> around after");
        return process;
    }


}