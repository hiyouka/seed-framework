package seed.seedframework.context.config;

import seed.seedframework.beans.definition.BeanDefinition;
import seed.seedframework.beans.definition.BeanDefinitionHolder;
import seed.seedframework.beans.definition.RootBeanDefinition;
import seed.seedframework.beans.factory.AutowiredAnnotationPostProcessor;
import seed.seedframework.beans.factory.BeanDefinitionRegistry;
import seed.seedframework.beans.metadata.AnnotatedTypeMetadata;
import seed.seedframework.common.AnnotationAttributes;
import seed.seedframework.util.BeanDefinitionReaderUtils;
import seed.seedframework.util.ClassUtils;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class AnnotationConfigUtils {

    public static final String CONFIGURATION_CLASS_PROCESSOR_BEAN_NAME = "seed.seedframework.context" +
            ".config.internalConfigurationClassPostProcessor";

    public static final String AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME = "seed.seedframework.context" +
            ".config.internalAutowiredAnnotationPostProcessor";

    public static final String AOP_ANNOTATION_PROCESSOR_BEAN_NAME = "seed.seedframework.aop" +
            ".aspect.internalAspectJAwareAdvisorAutoProxyCreator";

    private static final String AOP_ANNOTATION_PROCESSOR_CLASS_NAME = "seed.seedframework.aop"+
            ".aspect.AspectJAwareAdvisorAutoProxyCreator";

    public static AnnotationAttributes getAnnotationAttributes(AnnotatedTypeMetadata metadata, String annotationName){
        return AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(annotationName));
    }


    public static Set<BeanDefinitionHolder> registerAnnotationConfigProcessors(BeanDefinitionRegistry registry){
        Set<BeanDefinitionHolder> result = new LinkedHashSet<>();

        // load all bean definition
        if(!registry.containsBeanDefinition(CONFIGURATION_CLASS_PROCESSOR_BEAN_NAME)){
            BeanDefinitionHolder configProcessor = registerPostProcessor(registry,CONFIGURATION_CLASS_PROCESSOR_BEAN_NAME
                    ,new RootBeanDefinition(ConfigurationClassPostProcessor.class));
            result.add(configProcessor);
        }
        // bean post processor for autowired
        if(!registry.containsBeanDefinition(AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME)){
            BeanDefinitionHolder configProcessor = registerPostProcessor(registry,AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME
                    ,new RootBeanDefinition(AutowiredAnnotationPostProcessor.class));
            result.add(configProcessor);
        }

        // bean post processor for aop
        if(ClassUtils.includeClassOnDefaultClassLoader(AOP_ANNOTATION_PROCESSOR_CLASS_NAME)
           && !registry.containsBeanDefinition(AOP_ANNOTATION_PROCESSOR_BEAN_NAME)){
            Class<?> autoProxyCreatorClass = ClassUtils.forNameSafe(AOP_ANNOTATION_PROCESSOR_CLASS_NAME);
            BeanDefinitionHolder configProcessor = registerPostProcessor(registry,AOP_ANNOTATION_PROCESSOR_BEAN_NAME
                    ,new RootBeanDefinition(autoProxyCreatorClass));
            result.add(configProcessor);
        }

        return result;
    }

    private static BeanDefinitionHolder registerPostProcessor(BeanDefinitionRegistry registry, String beanName, BeanDefinition beanDefinition){
        BeanDefinitionHolder configProcessor = new BeanDefinitionHolder(beanDefinition, beanName);
        BeanDefinitionReaderUtils.registerBeanDefinition(configProcessor,registry);
        return configProcessor;
    }

}