package hiyouka.seedframework.context.annotation;

import hiyouka.seedframework.beans.definition.AnnotationBeanNameGenerator;
import hiyouka.seedframework.beans.definition.BeanDefinition;
import hiyouka.seedframework.beans.definition.BeanDefinitionHolder;
import hiyouka.seedframework.beans.definition.BeanNameGenerator;
import hiyouka.seedframework.beans.factory.BeanDefinitionRegistry;
import hiyouka.seedframework.common.AnnotationAttributes;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class ClassPathBeanDefinitionScanner extends ClassPathScanningComponentProvider{

    private  BeanDefinitionRegistry registry;

    private BeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();


//    public void setRegistry(BeanDefinitionRegistry registry){
//        registry.
//    }

    public Set<BeanDefinitionHolder> parse(AnnotationAttributes scanAttributes, String declaringClass){
        return null;
    }


    protected Set<BeanDefinitionHolder> doScan(String... basePackages){
        org.springframework.util.Assert.notEmpty(basePackages, "At least one base package must be set");
        Set<BeanDefinitionHolder> beanDefinitionHolders = new LinkedHashSet<>();
        for(String basePackage : basePackages){
            Set<BeanDefinition> beanDefinitions = findBeanDefinitions(basePackage);
            for(BeanDefinition beanDefinition : beanDefinitions){
                processBeanDefinitionToPrefect(beanDefinition);
            }
        }
        return beanDefinitionHolders;
    }

    protected void processBeanDefinitionToPrefect(BeanDefinition beanDefinition){
        //TODO: add as scope lazy primary to perfect beanDefinition information
    }

}