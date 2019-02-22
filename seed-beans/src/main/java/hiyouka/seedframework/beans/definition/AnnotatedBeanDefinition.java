package hiyouka.seedframework.beans.definition;

import hiyouka.seedframework.beans.metadata.AnnotationMetadata;

/**
 * @author hiyouka
 * Date: 2019/1/28
 * @since JDK 1.8
 */
public interface AnnotatedBeanDefinition extends BeanDefinition {

    AnnotationMetadata getMetadata();

}