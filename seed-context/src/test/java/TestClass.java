import hiyouka.framework.test.config.TestConfiguration;
import hiyouka.seedframework.context.AnnotationConfigApplicationContext;
import hiyouka.seedframework.context.ApplicationContext;
import org.junit.jupiter.api.Test;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class TestClass {

    @Test
    public void applicationTest() throws NoSuchFieldException {

        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(TestConfiguration.class);
        Object testAutowired = applicationContext.getBean("testAutowired");
        System.out.println("666");
    }

}