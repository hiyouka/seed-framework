package hiyouka.seedframework.beans.metadata;

import org.springframework.core.type.MethodMetadata;

import java.util.Set;

/**
 * @author hiyouka
 * Date: 2019/1/28
 * @since JDK 1.8
 */
public interface AnnotationMetadata extends ClassMetadata,AnnotatedTypeMetadata{

    /**
     * get all annotation page;
     */
    Set<String> getAnnotationTypes();

    /**
     * 获取某个注解下的所有注解名
     */
    Set<String> getMetaAnnotationTypes(String annotationName);


    boolean hasAnnotation(String annotationName);

    boolean hasAnnotatedMethods(String annotationName);

    Set<MethodMetadata> getAnnotatedMethods(String annotationName);

}