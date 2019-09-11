package seed.seedframework.context;

import seed.seedframework.beans.definition.AnnotatedBeanDefinition;
import seed.seedframework.beans.definition.AnnotatedGenericBeanDefinition;
import seed.seedframework.beans.definition.BeanDefinitionHolder;
import seed.seedframework.beans.definition.BeanNameGenerator;
import seed.seedframework.beans.factory.BeanDefinitionRegistry;
import seed.seedframework.context.config.AnnotationBeanNameGenerator;
import seed.seedframework.context.config.AnnotationConfigUtils;
import seed.seedframework.util.Assert;
import seed.seedframework.util.BeanDefinitionReaderUtils;

import javax.annotation.Nullable;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class AnnotatedBeanDefinitionReader {

    private final BeanDefinitionRegistry registry;

    private BeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();

    public AnnotatedBeanDefinitionReader(BeanDefinitionRegistry registry){
        Assert.notNull(registry,"BeanDefinitionRegistry must not be null");
        this.registry = registry;
        AnnotationConfigUtils.registerAnnotationConfigProcessors(this.registry);
    }

    public BeanDefinitionRegistry getRegistry() {
        return registry;
    }

    public BeanNameGenerator getBeanNameGenerator() {
        return beanNameGenerator;
    }

    public void setBeanNameGenerator(BeanNameGenerator beanNameGenerator) {
        this.beanNameGenerator = beanNameGenerator;
    }

    public void register(Class<?>... classes){
        for(Class<?> clazz : classes){
            registerBean(clazz);
        }
    }

    public void registerBean(Class<?> annotatedClass){
        doRegisterBean(annotatedClass,null);
    }

    public void registerBean(Class<?> annotatedClass, String name) {
        doRegisterBean(annotatedClass,name);
    }

    protected void doRegisterBean(Class<?> annotatedClass, @Nullable String name){
        AnnotatedBeanDefinition annotatedBeanDefinition = new AnnotatedGenericBeanDefinition(annotatedClass);
        String beanName = name == null ? this.beanNameGenerator.generateBeanName(annotatedBeanDefinition,this.registry) : name;
        BeanDefinitionReaderUtils.processBeanDefinitionToPrefect(annotatedBeanDefinition);
        BeanDefinitionReaderUtils.registerBeanDefinition(new BeanDefinitionHolder(annotatedBeanDefinition,beanName),this.registry);
    }
}