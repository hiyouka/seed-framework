package seed.seedframework.beans.definition;

import seed.seedframework.beans.metadata.AnnotationMetadata;
import seed.seedframework.beans.metadata.MethodMetadata;

import javax.annotation.Nullable;

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