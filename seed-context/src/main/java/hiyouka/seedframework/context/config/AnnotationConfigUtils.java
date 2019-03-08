package hiyouka.seedframework.context.config;

import hiyouka.seedframework.beans.definition.BeanDefinitionHolder;
import hiyouka.seedframework.beans.definition.RootBeanDefinition;
import hiyouka.seedframework.beans.factory.BeanDefinitionRegistry;
import hiyouka.seedframework.beans.metadata.AnnotatedTypeMetadata;
import hiyouka.seedframework.common.AnnotationAttributes;
import hiyouka.seedframework.util.BeanDefinitionReaderUtils;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class AnnotationConfigUtils {

    public static final String CONFIGURATION_CLASS_PROCESSOR_BEAN_NAME = "hiyouka.seedframework.context.config.internalConfigurationClassPostProcessor";

    public static AnnotationAttributes getAnnotationAttributes(AnnotatedTypeMetadata metadata, String annotationName){
        return AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(annotationName));
    }

    public static Set<BeanDefinitionHolder> registerAnnotationConfigProcessors(BeanDefinitionRegistry registry){
        Set<BeanDefinitionHolder> result = new LinkedHashSet<>();
        if(!registry.containsBeanDefinition(CONFIGURATION_CLASS_PROCESSOR_BEAN_NAME)){
            BeanDefinitionHolder configProcessor = new BeanDefinitionHolder
                    (new RootBeanDefinition(ConfigurationClassPostProcessor.class), CONFIGURATION_CLASS_PROCESSOR_BEAN_NAME);
            new BeanDefinitionHolder(new RootBeanDefinition(ConfigurationClassPostProcessor.class), CONFIGURATION_CLASS_PROCESSOR_BEAN_NAME);
            BeanDefinitionReaderUtils.registerBeanDefinition(configProcessor,registry);
            result.add(configProcessor);
        }
        return result;
    }

}