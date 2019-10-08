package aspect;

import aspect.aop.AopBefore;
import aspect.aop.PointTest;
import aspect.bean.TestAutowired;
import aspect.config.TestConfiguration;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.aspectj.weaver.tools.JoinPointMatch;
import org.aspectj.weaver.tools.PointcutPrimitive;
import org.aspectj.weaver.tools.ShadowMatch;
import org.junit.jupiter.api.Test;
import seed.seedframework.aop.interceptor.AspectAdvisorManager;
import seed.seedframework.aop.interceptor.AspectJPointcutAdvisor;
import seed.seedframework.aop.interceptor.MethodBeforeAspectJAdvice;
import seed.seedframework.aop.matcher.AspectMethodMatcher;
import seed.seedframework.aop.proxy.AopProxyCreator;
import seed.seedframework.aop.proxy.DefaultAopProxyCreator;
import seed.seedframework.context.AnnotationConfigApplicationContext;
import seed.seedframework.context.ApplicationContext;
import seed.seedframework.core.env.Environment;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class AspectTest {

    @Test
    public void testParameterName() throws NoSuchMethodException {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(TestConfiguration.class);
//        Object testAutowired = applicationContext.getBean("testAutowired");
        TestAutowired bean = applicationContext.getBean(TestAutowired.class);
//
        System.out.println(bean.getClass());
        bean.testB(null);
        Environment environment = applicationContext.getEnvironment();
        String property = environment.getProperty("spring.aop.auto");
        System.out.println(property);
    }

    @Test
    public void testT(){
//        Class<?> targetClass = this.advised.getTargetClass();
//        return Proxy.newProxyInstance(targetClass.getClassLoader(),targetClass.getInterfaces(),new DynamicInitiatorsInvocationHandler(advised));
        Class<TestAutowired> testAutowiredClass = TestAutowired.class;
//        TestAutowired testAutowired = new TestAutowired(null);
//        testAutowired.setAuto("213");
//        testAutowired.setLi("666");
//        Object o = Proxy.newProxyInstance(testAutowiredClass.getClassLoader(), testAutowiredClass.getInterfaces(), new TestHandler(testAutowired));


        Enhancer enhancer = new Enhancer();
//        Callback[] callBacks = getCallBack();
//        Class[] types = new Class[callBacks.length];
//        for(int i=0; i<types.length; i++){
//            types[i] = callBacks[i].getClass();
//        }
        enhancer.setCallbackType(TestInterceptor.class);

//        enhancer.setCallbacks(callBacks);

//        enhancer.setCallback(new TestInterceptor(testAutowired));
//        enhancer.setInterfaces(getInterfaces());
        enhancer.setSuperclass(TestAutowired.class);
        enhancer.setUseFactory(true);
        TestAutowired o1 = (TestAutowired)enhancer.create();
        o1.testB(null );
        System.out.println(o1);
    }

    private class TestInterceptor implements MethodInterceptor{

        private Object target;

        public TestInterceptor(Object target) {
            this.target = target;
        }

        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            return methodProxy.invoke(target,objects);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TestInterceptor that = (TestInterceptor) o;
            return Objects.equals(target, that.target);
        }

        @Override
        public int hashCode() {
            return Objects.hash(target);
        }

        @Override
        public String toString() {
            return "TestInterceptor{" +
                    "target=" + target +
                    '}';
        }
    }

    private class TestHandler implements InvocationHandler{

        public Object target;

        public TestHandler(Object target) {
            this.target = target;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return null;
        }
    }



    @Test
    public void cglibTest() throws NoSuchMethodException {
//        Enhancer enhancer = new Enhancer();
//        enhancer.setSuperclass(AspectJTest.class);
//        enhancer.setCallback(new CglibAopProxy.DynamicInitiatorsInterceptor());
//        Object o = enhancer.create();
        Method before = PointTest.class.getDeclaredMethod("before");
        MethodBeforeAspectJAdvice advice = new MethodBeforeAspectJAdvice(before,new PointTest());
        AspectJPointcutAdvisor advisor = new AspectJPointcutAdvisor(advice);
        AspectAdvisorManager aspectAdvisorManager = new AspectAdvisorManager(TestAopProxy.class);
        aspectAdvisorManager.addAdvisor(advisor);
        aspectAdvisorManager.setTarget(new TestAopProxy());
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

    @Test
    public void testMatch() throws NoSuchMethodException {
//        PointcutParser pointcutParser = PointcutParser
//                .getPointcutParserSupportingSpecifiedPrimitivesAndUsingSpecifiedClassLoaderForResolution(
//                        SUPPORTED_PRIMITIVES, ClassUtils.getDefaultClassLoader());
//        PointcutExpression pointcutExpression = pointcutParser
//                .parsePointcutExpression("point()", PointTest.class, new PointcutParameter[0]);
        Method testBefore = TestAopProxy.class.getDeclaredMethod("testBefore");
//        ShadowMatch shadowMatch = pointcutExpression.matchesMethodExecution(testBefore);
        TestAopProxy testAopProxy = new TestAopProxy();
        AopProxyCreator creator = new DefaultAopProxyCreator();
        AspectAdvisorManager aspectAdvisorManager = new AspectAdvisorManager(testAopProxy.getClass());
        Method before = PointTest.class.getDeclaredMethod("before");
        aspectAdvisorManager.addAdvisor(new AspectJPointcutAdvisor(new MethodBeforeAspectJAdvice(before,testAopProxy)));
        aspectAdvisorManager.setTarget(testAopProxy);
        MethodBeforeAspectJAdvice advice = (MethodBeforeAspectJAdvice) aspectAdvisorManager.getAdvisors()[0].getAdvice();
        AspectMethodMatcher matcher = (AspectMethodMatcher) advice.getAspectPointcut().methodMatch();
        Object proxy = creator.createAopProxy(aspectAdvisorManager).getProxy();
        boolean match = matcher.match(testBefore);
        System.out.println(match);
        ShadowMatch showMatch = matcher.getShowMatch(testBefore);
        JoinPointMatch joinPointMatch = showMatch.matchesJoinPoint(proxy, testAopProxy,new Object[]{"123"});
        boolean matches = joinPointMatch.matches();
        System.out.println(matches);
    }

}