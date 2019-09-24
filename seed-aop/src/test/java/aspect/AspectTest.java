package aspect;

import net.sf.cglib.proxy.Enhancer;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.weaver.tools.PointcutPrimitive;
import org.junit.jupiter.api.Test;
import seed.seedframework.aop.proxy.CglibAopProxy;
import seed.seedframework.aop.util.AspectJUtil;

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

        @Before("aspect.PointcutTest.point()")
        public void before(JoinPoint joinPoint){
            System.out.println(">>>>>>>>>>>>>>>>before ");
        }

    }

    public static void main(String[] args) throws NoSuchMethodException {
        Method point = AspectJTest.class.getDeclaredMethod("point");
        Method before = AspectJTest.class.getDeclaredMethod("before", JoinPoint.class);
        System.out.println(AspectJUtil.isAspectJMethod(point));
        System.out.println(AspectJUtil.isAspectJMethod(before));
//        long start = System.currentTimeMillis();
//        PointcutParser pointcutParser = PointcutParser
//                .getPointcutParserSupportingSpecifiedPrimitivesAndUsingSpecifiedClassLoaderForResolution(
//                        SUPPORTED_PRIMITIVES, ClassUtils.getDefaultClassLoader());
//        //"execution(* seed.seedframework.aop..*(java.lang.reflect.Method)) || within(seed.seedframework.aop.proxy.*)"
//        PointcutParameter joinPoint = pointcutParser.createPointcutParameter("joinPoint", JoinPoint.class);
//        PointcutExpression expression = pointcutParser.parsePointcutExpression("point()", PointcutTest.class, new PointcutParameter[0]);
//        Method match = ExecutionExpressionMethodMatcher.class.getDeclaredMethod("match", Method.class);
//        Method getDefaultClassLoader = ClassUtils.class.getMethod("getDefaultClassLoader");
//        ShadowMatch shadowMatch = expression.matchesMethodExecution(match);
//        ShadowMatch shadowMatch1 = expression.matchesStaticInitialization(AspectPointcut.class);
//        System.out.println(shadowMatch1.alwaysMatches());
//        boolean b = shadowMatch.alwaysMatches();
//        System.out.println(b);
//        System.out.println(System.currentTimeMillis()  - start);
    }

    @Test
    public void cglibTest(){
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(AspectJTest.class);
        enhancer.setCallback(new CglibAopProxy.DynamicInitiatorsInterceptor());
        Object o = enhancer.create();
    }

}