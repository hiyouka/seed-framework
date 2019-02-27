package hiyouka.seedframework.beans.definition;

import com.sun.istack.internal.Nullable;
import hiyouka.seedframework.beans.metadata.AnnotationMetadata;
import hiyouka.seedframework.beans.metadata.MethodMetadata;
import hiyouka.seedframework.beans.metadata.StandardAnnotationMetadata;
import hiyouka.seedframework.util.Assert;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class AnnotatedGenericBeanDefinition extends GenericBeanDefinition implements AnnotatedBeanDefinition {


    private final AnnotationMetadata metadata;

    /**
     * 用于@Bean注解的方法信息
     */
    @Nullable
    private MethodMetadata factoryMethodMetadata;

    public AnnotatedGenericBeanDefinition(Class<?> beanClass) {
        setBeanClass(beanClass);
        this.metadata = new StandardAnnotationMetadata(beanClass);
    }

    public AnnotatedGenericBeanDefinition(AnnotationMetadata metadata) {
        Assert.notNull(metadata, "AnnotationMetadata must not be null");
        if (metadata instanceof StandardAnnotationMetadata) {
            setBeanClass(((StandardAnnotationMetadata) metadata).getIntrospectedClass());
        }
        else {
            setBeanClassName(metadata.getClassName());
        }
        this.metadata = metadata;
    }

    @Override
    public AnnotationMetadata getMetadata() {
        return this.metadata;
    }

    @Override
    @Nullable
    public final MethodMetadata getFactoryMethodMetadata() {
        return this.factoryMethodMetadata;
    }
}