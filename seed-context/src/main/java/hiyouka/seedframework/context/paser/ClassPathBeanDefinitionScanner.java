package hiyouka.seedframework.context.paser;

import hiyouka.seedframework.beans.annotation.Lazy;
import hiyouka.seedframework.beans.annotation.Primary;
import hiyouka.seedframework.beans.annotation.Scope;
import hiyouka.seedframework.beans.definition.*;
import hiyouka.seedframework.beans.factory.BeanDefinitionRegistry;
import hiyouka.seedframework.beans.factory.config.ConfigurableBeanFactory;
import hiyouka.seedframework.beans.metadata.AnnotatedTypeMetadata;
import hiyouka.seedframework.beans.metadata.AnnotationMetadata;
import hiyouka.seedframework.common.AnnotationAttributes;
import hiyouka.seedframework.exception.BeanDefinitionStoreException;
import hiyouka.seedframework.util.Assert;
import hiyouka.seedframework.util.BeanDefinitionReaderUtils;
import hiyouka.seedframework.util.ClassUtils;
import hiyouka.seedframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class ClassPathBeanDefinitionScanner extends ClassPathScanningComponentProvider{

    private  BeanDefinitionRegistry registry;

    private BeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();

    public ClassPathBeanDefinitionScanner(){
        super();
    }

    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry){
        super();
        this.registry = registry;
    }

    public Set<BeanDefinitionHolder> parse(AnnotationAttributes componentScan, String declaringClass){
        Set<String> basePackages = new LinkedHashSet<>();
        String[] packages = componentScan.getStringArray("value");
        Collections.addAll(basePackages,packages);
        if(packages.length == 0){
            basePackages.add(ClassUtils.getPackageName(declaringClass));
        }
        return doScan(StringUtils.toStringArray(basePackages));
    }

    protected Set<BeanDefinitionHolder> doScan(String... basePackages){
        Assert.notEmpty(basePackages, "At least one base package must be set");
        Set<BeanDefinitionHolder> beanDefinitionHolders = new LinkedHashSet<>();
        for(String basePackage : basePackages){
            Set<BeanDefinition> beanDefinitions = findBeanDefinitions(basePackage);
            for(BeanDefinition beanDefinition : beanDefinitions){
                String beanName = this.beanNameGenerator.generateBeanName(beanDefinition, this.registry);
                if(checkBeanName(beanName)){
                    throw new BeanDefinitionStoreException("this beanName is existing : " + beanName + " [ bean class : " + beanDefinition.getBeanClassName() + " ] ");
                }
                if(beanDefinition instanceof AnnotatedBeanDefinition){
                    processBeanDefinitionToPrefect((AnnotatedBeanDefinition) beanDefinition);
                }
                BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder(beanDefinition, beanName);
                beanDefinitionHolders.add(definitionHolder);
                registerBeanDefinition(definitionHolder,this.registry);
            }
        }
        return beanDefinitionHolders;
    }

    protected void registerBeanDefinition(BeanDefinitionHolder definitionHolder, BeanDefinitionRegistry registry) {
        BeanDefinitionReaderUtils.registerBeanDefinition(definitionHolder, registry);
    }

    protected boolean checkBeanName(String beanName) {
        return registry.containsBeanDefinition(beanName);
    }

    protected void processBeanDefinitionToPrefect(AnnotatedBeanDefinition beanDefinition){
        AnnotationMetadata metadata = beanDefinition.getMetadata();
        AnnotationAttributes scope = attributesFor(metadata, Scope.class);
        if(scope != null){
            beanDefinition.setScope(scope.getString("value"));
        }

        AnnotationAttributes lazy = attributesFor(metadata, Lazy.class);
        if(lazy != null){
            beanDefinition.setLazyInit(lazy.getBoolean("value"));
        }
        if(metadata.isAnnotated(Primary.class.getName())){
            beanDefinition.setPrimary(true);
        }
    }


    private boolean checkScopeVal(String val){
        return ConfigurableBeanFactory.SCOPE_SINGLETON.equals(val)
              || ConfigurableBeanFactory.SCOPE_PROTOTYPE.equals(val);
    }


    protected static AnnotationAttributes attributesFor(AnnotatedTypeMetadata metadata, Class<? extends Annotation> annotationClass){
        return AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(annotationClass.getName()));
    }

    public void setRegistry(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }
}