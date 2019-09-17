import hiyouka.framework.test.bean.TestAutowiredBean;
import hiyouka.framework.test.config.TestConfiguration;
import seed.seedframework.context.AnnotationConfigApplicationContext;
import seed.seedframework.context.ApplicationContext;
import seed.seedframework.core.env.Environment;
import org.junit.jupiter.api.Test;
import seed.seedframework.core.intercept.*;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class TestClass {

    String test = "123123";



    @Test
    public void applicationTest() throws NoSuchFieldException {

        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(TestConfiguration.class);
//        Object testAutowired = applicationContext.getBean("testAutowired");
        TestAutowiredBean bean = applicationContext.getBean(TestAutowiredBean.class);
        Environment environment = applicationContext.getEnvironment();
        String property = environment.getProperty("spring.aop.auto");
        List<String> list = new LinkedList<>();
        for(int i=0; i<10; i++){
            list.add(i+"");
        }

//        YmlProperties properties = new YmlProperties();
//        properties.load(getClass().getResourceAsStream("seed.yml"));
//        System.out.println(properties);
//        int i = test.indexOf("123");
//        int i1 = test.indexOf("123", 3);
//        System.out.println(i);
//        System.out.println(i1);

    }

    private String mainTest(String arg){
        System.out.println(">>>>>>>>>>mainTest<<<<<<<<<<" + arg);
        return "123";
    }

    @Test
    public void interceptorTest() throws Throwable {
        TestClass testClass = new TestClass();
        Method mainTest = testClass.getClass().getDeclaredMethod("mainTest", String.class);
        Object[] args = {"123"};
        InterceptorChainMethodInvocation invocation = new InterceptorChainMethodInvocation(mainTest,testClass,args);
        invocation.pushInterceptor(new MethodBeforeAdviceInterceptor((method, args1, target) -> {
            System.out.println("method before interceptor invoke .......... ");
        }));
        invocation.pushInterceptor(new AroundAdviceInterceptor(new MethodAroundAdvice() {
            @Override
            public Object around(MethodInvocation invocation) throws Throwable {
//                System.out.println("method around interceptor invoke before ........");
//                System.out.println("method around interceptor invoke after ........");

                return invocation.process();
            }
        }));
        invocation.pushInterceptor(new MethodAfterReturnAdviceInterceptor((returnVal, method, args12, target) -> {
            System.out.println("method after interceptor invoke .........");
            return null;
        }));
        Object process = invocation.process();
        System.out.println(process);
    }

}