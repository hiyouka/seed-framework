package hiyouka.seedframework.beans.test;

import hiyouka.seedframework.beans.definition.AnnotatedGenericBeanDefinition;
import hiyouka.seedframework.beans.definition.AnnotationBeanNameGenerator;
import hiyouka.seedframework.beans.definition.BeanDefinition;
import org.junit.jupiter.api.Test;

/**
 * @author hiyouka
 * Date: 2019/2/21
 * @since JDK 1.8
 */
public class TestClass {

    @Test
    public void test() throws NoSuchMethodException {
//        System.out.println(Tesscls.class.getDeclaringClass());
//        System.out.println(Tesscls.Sta.class.getDeclaringClass());
//        System.out.println(Tesscls.class.getEnclosingClass());
//        System.out.println(Tesscls.Sta.class.getEnclosingClass());
//        System.out.println(Tesscls.Sta.class.getEnclosingClass());
//        StandardClassMetadata standardClassMetadata = new StandardClassMetadata(Tesscls.class);
//        boolean independent = standardClassMetadata.isIndependent();
//
//        System.out.println(Arrays.asList(standardClassMetadata.getMemberClassNames()));
//        Method test = AnoTest.class.getDeclaredMethod("test");
//        MethodMetadata methodMetadata = new StandardMethodMetadata(test);
//        AnnotatedElementUtils
//        String declaringClassName = methodMetadata.getDeclaringClassName();
//        System.out.println(methodMetadata.getMethodName());
//        System.out.println(methodMetadata.getReturnTypeName());
//        System.out.println(declaringClassName);
        BeanDefinition beanDefinition = new AnnotatedGenericBeanDefinition(AnoTest.class);
        AnnotationBeanNameGenerator annotationBeanNameGenerator = new AnnotationBeanNameGenerator();
        String beanName = annotationBeanNameGenerator.generateBeanName(beanDefinition, null);
        System.out.println(beanName);
    }

}