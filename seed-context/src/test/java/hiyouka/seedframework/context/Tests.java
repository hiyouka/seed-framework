package hiyouka.seedframework.context;

import hiyouka.seedframework.beans.annotation.ComponentScan;
import hiyouka.seedframework.beans.definition.BeanDefinitionHolder;
import hiyouka.seedframework.beans.factory.DefaultBenFactory;
import hiyouka.seedframework.common.AnnotationAttributes;
import hiyouka.seedframework.context.paser.ClassPathBeanDefinitionScanner;
import hiyouka.seedframework.util.AnnotatedElementUtils;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class Tests {


    @Test
    public void test(){
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner();
        DefaultBenFactory defaultBenFactory = new DefaultBenFactory();
        scanner.setRegistry(defaultBenFactory);
        AnnotationAttributes annotationAttributes = AnnotatedElementUtils.getAnnotationAttributes(ConfigClass.class, ComponentScan.class);
        Set<BeanDefinitionHolder> parse = scanner.parse(annotationAttributes, ConfigClass.class.getName());
        String[] beanDefinitionNames = defaultBenFactory.getBeanDefinitionNames();
        System.out.println(Arrays.asList(beanDefinitionNames));
        Class<? extends Annotation> annotationType = AnnotatedElementUtils.getAnnotationType(ConfigClass.class, null);
    }


}