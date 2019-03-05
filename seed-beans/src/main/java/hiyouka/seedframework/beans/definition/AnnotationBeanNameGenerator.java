package hiyouka.seedframework.beans.definition;

import hiyouka.seedframework.beans.annotation.Component;
import hiyouka.seedframework.beans.factory.BeanDefinitionRegistry;
import hiyouka.seedframework.beans.metadata.AnnotationMetadata;
import hiyouka.seedframework.common.AnnotationAttributes;
import hiyouka.seedframework.util.*;

import java.beans.Introspector;
import java.util.Map;
import java.util.Set;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class AnnotationBeanNameGenerator implements BeanNameGenerator {

    public static final String COMPONENT_ANNOTATION_CLASSNAME = "hiyouka.seedframework.beans.annotation.Component";

    public static final String BEAN_ANNOTATION_CLASSNAME = "hiyouka.seedframework.beans.annotation.Bean";

    public static final String IMPORT_ANNOTATION_CLASSNAME = "hiyouka.seedframework.beans.annotation.Import";

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
            else if(isBean(attributes,amd.getMetaAnnotationTypes(type),type)){
                Object name = attributes.get("name");
                if(name instanceof String){
                    String val = (String) name;
                    if(StringUtils.hasText(val)){
                        beanName = val;
                    }
                }
                if(!StringUtils.hasText(beanName)){
                    beanName = annotatedDef.getFactoryMethodName();
                }
            }
            else if(isImport(amd.getMetaAnnotationTypes(type), type)){
                beanName = annotatedDef.getBeanClassName();
            }
        }
        Map<String, Object> annotationAttributes = amd.getAnnotationAttributes(Component.class.getName());
        if(!CollectionUtils.isEmpty(annotationAttributes)){
            Object value = annotationAttributes.get("value");
            if(value instanceof String)
                beanName = (String) value;
        }
        return beanName;
    }

    private boolean isBean(AnnotationAttributes attributes, Set<String> metaAnnotationTypes, String annotationType) {
        boolean isBean = BEAN_ANNOTATION_CLASSNAME.equals(annotationType)
                || metaAnnotationTypes.contains(BEAN_ANNOTATION_CLASSNAME);
        return (isBean && attributes != null && attributes.containsKey("name"));
    }


    private boolean isImport(Set<String> metaAnnotationTypes, String annotationType) {
        return IMPORT_ANNOTATION_CLASSNAME.equals(annotationType)
                || metaAnnotationTypes.contains(IMPORT_ANNOTATION_CLASSNAME);
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
        Assert.state(beanClassName != null, "No bean class name set");
        String shortClassName = ClassUtils.getShortName(beanClassName);
        return Introspector.decapitalize(shortClassName);
    }
}