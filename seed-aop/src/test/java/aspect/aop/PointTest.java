package aspect.aop;

import aspect.bean.TestBean1;
import org.aspectj.lang.JoinPoint;
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
    @Before(value = "aspect.PointcutTest.point() || @annotation(aspect.aop.AopBefore)",argNames = "join")
    public void before(JoinPoint join){
        System.out.println(">>>>>>>>>>>>>>>>before ");
    }

    @Order(4)
    @AfterThrowing(value = "aspect.PointcutTest.point() || @annotation(aspect.aop.AopBefore)" , throwing = "e",argNames = "e,joinPoint")
    public void afterThrow(Throwable e, JoinPoint joinPoint){
        System.out.println(">>>>>>>>>>>>>afterThrow" + e);
    }

    @Order(3)
    @After("aspect.PointcutTest.point() || @annotation(aspect.aop.AopBefore)")
    public void after(JoinPoint joinPoint){
        System.out.println(">>>>>>>>>>>>>>>after");
    }

    @Order(2)
    @AfterReturning(value = "aspect.PointcutTest.point() || @annotation(aspect.aop.AopBefore)", returning = "ret")
    public void afterReturn(TestBean1<String,String> ret, JoinPoint joinPoint){

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