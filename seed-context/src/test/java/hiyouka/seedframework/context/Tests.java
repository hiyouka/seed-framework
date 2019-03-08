package hiyouka.seedframework.context;

import org.junit.jupiter.api.Test;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class Tests {


    @Test
    public void test() {
//
//        Method getLess = BeanUtils.findMethod(AnoTest.class, "getLess", null);
//        BeanDefinitionRegistry registry = new DefaultBenFactory();
//        registry.registerBeanDefinition("config",new AnnotatedGenericBeanDefinition(ConfigClass.class));
////        ConfigurationClassPostProcessor postProcessor = new ConfigurationClassPostProcessor();
////        postProcessor.postProcessBeanDefinitionRegistry(registry);
//        registry.registerBeanDefinition("configProcessor", new RootBeanDefinition(ConfigurationClassPostProcessor.class));
//
//        if(registry instanceof BeanFactory){
//            ConfigurationClassPostProcessor bean1 = ((BeanFactory) registry).getBean(ConfigurationClassPostProcessor.class);
//            bean1.postProcessBeanDefinitionRegistry(registry);
//            AnoTest bean = ((BeanFactory) registry).getBean(AnoTest.class);
//        }

//        System.out.println(registry.getBeanDefinitionCount());
//        for(String beanName : registry.getBeanDefinitionNames()){
//            BeanDefinition beanDefinition = registry.getBeanDefinition(beanName);
//            System.out.println(beanName);
//        }
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(ConfigClass.class);
        AnoTest bean = applicationContext.getBean(AnoTest.class);
    }

}