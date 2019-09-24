package aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.weaver.tools.*;
import org.junit.jupiter.api.Test;
import seed.seedframework.aop.ExecutionExpressionMethodMatcher;
import seed.seedframework.aop.interceptor.AspectAdvisorManager;
import seed.seedframework.aop.interceptor.AspectJPointcutAdvisor;
import seed.seedframework.aop.interceptor.MethodBeforeAspectJAdvice;
import seed.seedframework.aop.pointcut.AspectPointcut;
import seed.seedframework.aop.proxy.DefaultAopProxyCreator;
import seed.seedframework.aop.util.AspectJUtil;
import seed.seedframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class AspectTest {

    private static final Set<PointcutPrimitive> SUPPORTED_PRIMITIVES = new HashSet<>();

    static {
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.EXECUTION);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.ARGS);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.REFERENCE);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.THIS);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.TARGET);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.WITHIN);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_ANNOTATION);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_WITHIN);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_ARGS);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_TARGET);
    }

    @Aspect
    private class AspectJTest{

        @Pointcut(value = "execution(* seed.seedframework.aop..*(..))")
        private void point(){};

        @Before("aspect.PointcutTest.point() || @annotation(aspect.AopBefore)")
        public void before(){
            System.out.println(">>>>>>>>>>>>>>>>before ");
        }

    }

    public static void main(String[] args) throws NoSuchMethodException {
        Method point = AspectJTest.class.getDeclaredMethod("point");
        Method before = AspectJTest.class.getDeclaredMethod("before");
        System.out.println(AspectJUtil.isAspectJMethod(point));
        System.out.println(AspectJUtil.isAspectJMethod(before));
        long start = System.currentTimeMillis();
        PointcutParser pointcutParser = PointcutParser
                .getPointcutParserSupportingSpecifiedPrimitivesAndUsingSpecifiedClassLoaderForResolution(
                        SUPPORTED_PRIMITIVES, ClassUtils.getDefaultClassLoader());
        //"execution(* seed.seedframework.aop..*(java.lang.reflect.Method)) || within(seed.seedframework.aop.proxy.*)"
        PointcutParameter joinPoint = pointcutParser.createPointcutParameter("joinPoint", JoinPoint.class);
        PointcutExpression expression = pointcutParser.parsePointcutExpression("aspect.PointcutTest.point()", AspectJTest.class, new PointcutParameter[0]);
        Method match = ExecutionExpressionMethodMatcher.class.getDeclaredMethod("match", Method.class);
        Method getDefaultClassLoader = ClassUtils.class.getMethod("getDefaultClassLoader");
        ShadowMatch shadowMatch = expression.matchesMethodExecution(match);
        ShadowMatch shadowMatch1 = expression.matchesStaticInitialization(AspectPointcut.class);
        Method testBefore = TestAopProxy.class.getDeclaredMethod("testBefore");
        ShadowMatch shadowMatch2 = expression.matchesMethodExecution(testBefore);
        System.out.println(">>>>>" + shadowMatch2.alwaysMatches());
        System.out.println(shadowMatch1.alwaysMatches());
        boolean b = shadowMatch.alwaysMatches();
        System.out.println(b);
        System.out.println(System.currentTimeMillis()  - start);
    }

    @Test
    public void cglibTest() throws NoSuchMethodException {
//        Enhancer enhancer = new Enhancer();
//        enhancer.setSuperclass(AspectJTest.class);
//        enhancer.setCallback(new CglibAopProxy.DynamicInitiatorsInterceptor());
//        Object o = enhancer.create();
        Method before = AspectJTest.class.getDeclaredMethod("before");
        MethodBeforeAspectJAdvice advice = new MethodBeforeAspectJAdvice(before,new AspectJTest());
        AspectJPointcutAdvisor advisor = new AspectJPointcutAdvisor(advice);
        AspectAdvisorManager aspectAdvisorManager = new AspectAdvisorManager(TestAopProxy.class);
        aspectAdvisorManager.addAdvisor(advisor);
//        CglibAopProxy cglibAopProxy = new CglibAopProxy(aspectAdvisorManager);
//        TestAopProxy proxy = (TestAopProxy) cglibAopProxy.getProxy();
//        proxy.testBefore();
        TestAopInterface proxy = (TestAopInterface) new DefaultAopProxyCreator()
                .createAopProxy(aspectAdvisorManager).getProxy();
        proxy.testBefore();

    }

    public interface TestAopInterface{

        void testBefore();

    }

    public static class TestAopProxy implements TestAopInterface{

        @AopBefore
        public void testBefore(){
            System.out.println("testBefore<<<<<<<<<<<<");
        }

    }

}