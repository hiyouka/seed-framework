package seed.seedframework.context.config;

import seed.seedframework.beans.definition.AnnotatedBeanDefinition;
import seed.seedframework.beans.definition.BeanDefinition;
import seed.seedframework.beans.definition.BeanNameGenerator;
import seed.seedframework.beans.factory.BeanDefinitionRegistry;
import seed.seedframework.beans.metadata.AnnotationMetadata;
import seed.seedframework.common.AnnotationAttributes;
import seed.seedframework.util.Assert;
import seed.seedframework.util.ClassUtils;
import seed.seedframework.util.StringUtils;

import java.beans.Introspector;
import java.util.Set;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class AnnotationBeanNameGenerator implements BeanNameGenerator {

    public static final String COMPONENT_ANNOTATION_CLASSNAME = "seed.seedframework.beans.annotation.Component";



    @Override
    public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
        if(definition instanceof AnnotatedBeanDefinition){
            String beanName = determineBeanNameFromAnnotation((AnnotatedBeanDefinition) definition);
            if(StringUtils.hasText(beanName)){
                return beanName;
            }
        }
        return buildDefaultBeanName(definition,registry);
    }


    protected String buildDefaultBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
        return buildDefaultBeanName(definition);
    }


    protected String determineBeanNameFromAnnotation(AnnotatedBeanDefinition annotatedDef) {
        AnnotationMetadata amd = annotatedDef.getMetadata();
        String beanName = null;
        Set<String> types = amd.getAnnotationTypes();
        for(String type : types){
            AnnotationAttributes attributes = AnnotationConfigUtils.getAnnotationAttributes(amd, type);
            if(isComponent(attributes,amd.getMetaAnnotationTypes(type),type)){
                Object value = attributes.get("value");
                if(value instanceof String){
                    String val = (String) value;
                    if(StringUtils.hasText(val)){
                        beanName = val;
                    }
                }
            }

        }
        return beanName;
    }



    /**
     *  判断是否包含组件注解
     */
    protected boolean isComponent(AnnotationAttributes attributes,Set<String> metaAnnotationTypes, String annotationType){
        boolean isComponent = COMPONENT_ANNOTATION_CLASSNAME.equals(annotationType)
                    || metaAnnotationTypes.contains(COMPONENT_ANNOTATION_CLASSNAME);
        return (isComponent && attributes != null && attributes.containsKey("value"));
    }


    protected String buildDefaultBeanName(BeanDefinition definition) {
        String beanClassName = definition.getBeanClassName();
        Assert.state(beanClassName != null, "No hiyouka.framework.test.bean class name set");
        String shortClassName = ClassUtils.getShortName(beanClassName);
        return Introspector.decapitalize(shortClassName);
    }
}