package hiyouka.seedframework.context.paser;

import hiyouka.seedframework.beans.annotation.*;
import hiyouka.seedframework.beans.definition.*;
import hiyouka.seedframework.beans.exception.BeanDefinitionStoreException;
import hiyouka.seedframework.beans.factory.BeanDefinitionRegistry;
import hiyouka.seedframework.beans.factory.config.ConfigurableBeanFactory;
import hiyouka.seedframework.beans.metadata.AnnotatedTypeMetadata;
import hiyouka.seedframework.beans.metadata.AnnotationMetadata;
import hiyouka.seedframework.beans.metadata.MethodMetadata;
import hiyouka.seedframework.common.AnnotationAttributes;
import hiyouka.seedframework.context.config.filter.AnnotationIncludeFilter;
import hiyouka.seedframework.context.config.filter.ClassTypeFilter;
import hiyouka.seedframework.util.Assert;
import hiyouka.seedframework.util.BeanDefinitionReaderUtils;
import hiyouka.seedframework.util.ClassUtils;
import hiyouka.seedframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.util.*;

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
        //default
        addExcludeFilters(new ClassTypeFilter(ClassUtils.getClass(declaringClass)));
        //default
        addIncludeFilters(new AnnotationIncludeFilter(Component.class));

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
                BeanDefinitionHolder definitionHolder = registerOriginBeanDefinition(beanDefinition);
                beanDefinitionHolders.add(definitionHolder);
            }
        }
        return beanDefinitionHolders;
    }

    protected BeanDefinitionHolder registerOriginBeanDefinition(BeanDefinition beanDefinition){
        if(beanDefinition instanceof AnnotatedBeanDefinition){
            processBeanDefinitionToPrefect((AnnotatedBeanDefinition) beanDefinition);
        }
        String beanName = generateBeanName(beanDefinition);
        BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder(beanDefinition, beanName);
        registerBeanDefinition(definitionHolder,this.registry);
        return definitionHolder;
    }

    protected void registerBeanDefinition(BeanDefinitionHolder definitionHolder, BeanDefinitionRegistry registry) {
        BeanDefinitionReaderUtils.registerBeanDefinition(definitionHolder, registry);
        logger.info("register bean name : " + definitionHolder.getBeanName() + ", bean class ["+definitionHolder.getBeanDefinition().getBeanClassName()+"]");
    }

    protected String generateBeanName(BeanDefinition beanDefinition){
        String beanName = this.beanNameGenerator.generateBeanName(beanDefinition, this.registry);
        if(checkBeanName(beanName)){
            throw new BeanDefinitionStoreException("this beanName is existing : " + beanName + " [ bean class : " + beanDefinition.getBeanClassName() + " ] ");
        }
        return beanName;
    }

    protected boolean checkBeanName(String beanName) {
        return registry.containsBeanDefinition(beanName);
    }

    protected void processBeanDefinitionToPrefect(AnnotatedBeanDefinition beanDefinition){
        AnnotationMetadata metadata = beanDefinition.getMetadata();
        if(beanDefinition instanceof AbstractBeanDefinition){
            String initMethod = processInitAndDestroyMethod(beanDefinition, metadata, true);
            String destroyMethod = processInitAndDestroyMethod(beanDefinition, metadata, false);
            ((AbstractBeanDefinition) beanDefinition).setInitMethodName(initMethod);
            ((AbstractBeanDefinition) beanDefinition).setDestroyMethodName(destroyMethod);
        }

        processBeanDefinition(metadata,beanDefinition);
    }

    /** get intiMethod or destroyMethod name */
    protected String processInitAndDestroyMethod(BeanDefinition beanDefinition, AnnotationMetadata metadata, boolean isInit){
        String annotationName;
        String methodName;
        String result = null;
        if(isInit){
            annotationName = InitMethod.class.getName();
            methodName = " inti method ";
        }else {
            annotationName = DestroyMethod.class.getName();
            methodName = " destroy method ";
        }
        Set<MethodMetadata> methods = metadata.getAnnotatedMethods(annotationName);
        if(methods.size() > 1){
            List<String> methodsNames = new ArrayList<>();
            for(MethodMetadata methodMetadata : methods){
                methodsNames.add(methodMetadata.getMethodName());
            }
            throw new IllegalStateException(methodName + " must unique there is " + methodsNames.size()
                    + methodName + methodsNames + "in class : " + beanDefinition.getBeanClassName());
        }
        if(methods.size() != 0){
            for(MethodMetadata methodMetadata : methods){
                if(isVoidMethod(methodMetadata)){
                    result = methodMetadata.getMethodName();
                }else {
                    throw new IllegalStateException(methodName + "must no return type, method: "+
                                methodMetadata.getMethodName()+" have return type : "
                                +methodMetadata.getReturnTypeName()+", in class : "
                                + beanDefinition.getBeanClassName());
                }
            }
        }
        return result;
    }

    protected boolean isVoidMethod(MethodMetadata methodMetadata){
        return "void".equals(methodMetadata.getReturnTypeName());
    }

    protected void processBeanDefinition(AnnotatedTypeMetadata metadata, BeanDefinition beanDefinition){
        AnnotationAttributes scope = attributesFor(metadata, Scope.class);
        if(scope != null){
            beanDefinition.setScope(scope.getString("value"));
        }else {
            // set default scope
            beanDefinition.setScope(ConfigurableBeanFactory.SCOPE_SINGLETON);
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