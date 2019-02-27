package hiyouka.seedframework.beans.definition;

import com.sun.istack.internal.Nullable;
import hiyouka.seedframework.beans.metadata.AnnotationMetadata;
import hiyouka.seedframework.beans.metadata.MethodMetadata;

/**
 * @author hiyouka
 * Date: 2019/1/28
 * @since JDK 1.8
 */
public interface AnnotatedBeanDefinition extends BeanDefinition {

    AnnotationMetadata getMetadata();

    @Nullable
    MethodMetadata getFactoryMethodMetadata();

}