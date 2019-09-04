import hiyouka.framework.test.bean.TestBean1;
import hiyouka.seedframework.beans.annotation.Autowired;
import hiyouka.seedframework.beans.annotation.Bean;
import hiyouka.seedframework.util.AnnotatedElementUtils;
import hiyouka.seedframework.util.AnnotationUtils;
import hiyouka.seedframework.util.MultiValueMap;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class TestClass {

    @Test
    public void applicationTest(){
        MultiValueMap<String, Object> attributes = AnnotatedElementUtils.getAttributes(TestBean1.class, Autowired.class);
        List<Method> attributeMethods = AnnotationUtils.getAttributeMethods(Bean.class);

//        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(TestConfiguration.class);
        System.out.println(attributeMethods);
    }

}