import hiyouka.framework.test.bean.TestBean1;
import hiyouka.seedframework.beans.annotation.Autowired;
import hiyouka.seedframework.beans.annotation.Bean;
import hiyouka.seedframework.core.annotation.Priority;
import hiyouka.seedframework.util.AnnotatedElementUtils;
import hiyouka.seedframework.util.AnnotationUtils;
import hiyouka.seedframework.util.MultiValueMap;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class TestClass {

    @Test
    public void applicationTest() throws NoSuchFieldException {
        MultiValueMap<String, Object> attributes = AnnotatedElementUtils.getAttributes(TestBean1.class, Autowired.class);
        Object value = AnnotatedElementUtils.getAttribute(TestBean1.class, Priority.class, "value");
        List<Method> attributeMethods = AnnotationUtils.getAttributeMethods(Bean.class);
        MultiValueMap<String, Object> attributes1 = AnnotatedElementUtils.getAttributes(TestBean1.class, Priority.class);
        Field bean1 = TestBean1.class.getDeclaredField("bean1");
        Type genericType = bean1.getGenericType();
//        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(TestConfiguration.class);
        System.out.println(attributeMethods);
    }

}