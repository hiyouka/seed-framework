package hiyouka.seedframework.util;

import hiyouka.seedframework.beans.annotation.*;
import hiyouka.seedframework.beans.definition.AbstractBeanDefinition;
import hiyouka.seedframework.beans.definition.AnnotatedBeanDefinition;
import hiyouka.seedframework.beans.definition.BeanDefinition;
import hiyouka.seedframework.beans.definition.BeanDefinitionHolder;
import hiyouka.seedframework.beans.exception.BeanDefinitionStoreException;
import hiyouka.seedframework.beans.factory.BeanDefinitionRegistry;
import hiyouka.seedframework.beans.factory.config.ConfigurableBeanFactory;
import hiyouka.seedframework.beans.metadata.AnnotatedTypeMetadata;
import hiyouka.seedframework.beans.metadata.AnnotationMetadata;
import hiyouka.seedframework.beans.metadata.MethodMetadata;
import hiyouka.seedframework.common.AnnotationAttributes;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class BeanDefinitionReaderUtils {

    public static void registerBeanDefinition(BeanDefinitionHolder definitionHolder, BeanDefinitionRegistry registry)
            throws BeanDefinitionStoreException {
        String beanName = definitionHolder.getBeanName();
        registry.registerBeanDefinition(beanName, definitionHolder.getBeanDefinition());
    }


    public static void processBeanDefinition(AnnotatedTypeMetadata metadata, BeanDefinition beanDefinition){
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


    public static void processBeanDefinitionToPrefect(AnnotatedBeanDefinition beanDefinition){
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
    public static String processInitAndDestroyMethod(BeanDefinition beanDefinition, AnnotationMetadata metadata, boolean isInit){
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

    public static boolean isVoidMethod(MethodMetadata methodMetadata){
        return "void".equals(methodMetadata.getReturnTypeName());
    }

    public static AnnotationAttributes attributesFor(AnnotatedTypeMetadata metadata, Class<? extends Annotation> annotationClass){
        return AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(annotationClass.getName()));
    }


}