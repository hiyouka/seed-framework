import hiyouka.framework.test.config.TestConfiguration;
import seed.seedframework.context.AnnotationConfigApplicationContext;
import seed.seedframework.context.ApplicationContext;
import seed.seedframework.core.env.Environment;
import org.junit.jupiter.api.Test;

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
        Object testAutowired = applicationContext.getBean("testAutowired");
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

}