package hiyouka.seedframework;

import hiyouka.seedframework.util.AnnotatedElementUtils;
import hiyouka.seedframework.util.AnnotationUtils;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

/**
 * @author hiyouka
 * Date: 2019/2/21
 * @since JDK 1.8
 */
public class test {

    @Test
    public void test() throws NoSuchMethodException {
        List<Method> attributeMethods = AnnotationUtils.getAttributeMethods(Tess.class);
//        System.out.println(attributeMethods);
        attributeMethods.forEach(method -> {
            System.out.println(method);
            System.out.println("======================");
        });

        ThreadLocal<String> threadLocal = new ThreadLocal<>();
        threadLocal.set("214");
//        Tess declaredAnnotation = AnoTest.class.getDeclaredAnnotation(Tess.class);
//        Object value = AnnotationUtils.getAttribute("ke", declaredAnnotation);
//        System.out.println(value);

//        Map<String, Object> attributes = AnnotationUtils.getAttributes(declaredAnnotation);
//        Object attribute = AnnotatedElementUtils.getAttributes(AnoTest.class, Tess.class);
//        Method tess = AnoTest.class.getMethod("test");
//        MultiValueMap<String, Object> attribute1 = AnnotatedElementUtils.getAttributes(tess, Tess.class);
//        Object value = AnnotatedElementUtils.getAttribute(tess, Tess.class, "value");
//
//        System.out.println(AnnotatedElementUtils.isAnnotated(tess,Tess.class.getName()));
//
//        AnnotationAttributes annotationAttributes = AnnotatedElementUtils.getAnnotationAttributes(AnoTest.class, Tess.class);
        Set<String> metaAnnotationTypes = AnnotatedElementUtils.getMetaAnnotationTypes(AnoTest.class, Tell.class.getName());
        System.out.println("");
//        System.out.println(Arrays.asList(metaAnnotationTypes.toArray()));
//        List<Object> objects = Arrays.asList(null);
//        for (AnnotationAttributes annotationAttributes1 : annotationAttributes){
//            Class<? extends Annotation> aClass = annotationAttributes1.annotationType();
//            System.out.println(aClass);
//        }

//        org.springframework.util.MultiValueMap<String, Object> allAnnotationAttributes = org.springframework.core.annotation.AnnotatedElementUtils.getAllAnnotationAttributes(tess, Tess.class.getName());

//        AnnotationUtils.
//        AnoTest.class.getD
        Class<AnoTest.innerClass> innerClassClass = AnoTest.innerClass.class;
//        AnoTest.innerClass innerClass = BeanUtils.instanceClass(innerClassClass);


    }


}