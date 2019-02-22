package hiyouka.seedframework.beans.metadata;

import hiyouka.seedframework.util.AnnotatedElementUtils;
import hiyouka.seedframework.util.MultiValueMap;
import org.springframework.core.type.MethodMetadata;

import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author hiyouka
 * Date: 2019/2/21
 * @since JDK 1.8
 */
public class StandardAnnotationMetadata extends StandardClassMetadata implements AnnotationMetadata {

    private final Annotation[] annotations;

    public StandardAnnotationMetadata(Class<?> introspectedClass) {
        super(introspectedClass);
        this.annotations = introspectedClass.getAnnotations();
    }

    public Set<String> getAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        for (Annotation ann : this.annotations) {
            types.add(ann.annotationType().getName());
        }
        return types;
    }

    @Override
    public Set<String> getMetaAnnotationTypes(String annotationName) {
        return null;
    }

    @Override
    public boolean hasAnnotation(String annotationName) {
        return isAnnotated(annotationName);
    }


    @Override
    public boolean hasAnnotatedMethods(String annotationName) {
        return false;
    }

    @Override
    public Set<MethodMetadata> getAnnotatedMethods(String annotationName) {
        return null;
    }


    @Override
    public boolean isAnnotated(String annotationName) {
        Class<? extends Annotation> annotationType = getAnnotationType(annotationName);
        return AnnotatedElementUtils.isAnnotated(getIntrospectedClass(),annotationType);
    }

    @Override
    public MultiValueMap<String, Object> getAnnotationAttributes(String annotationName) {
        Class<? extends Annotation> annotationType = getAnnotationType(annotationName);
        return (this.annotations.length > 0 ? AnnotatedElementUtils.getAttributes(
                getIntrospectedClass(), annotationType) : null);
    }

    private Class<? extends Annotation> getAnnotationType(String annotationName){
        if(annotations == null || annotations.length == 0)
            return null;
        Class<? extends Annotation> clazz = null;
        for(Annotation annotation : annotations){
            if(annotation.getClass().getName().equals(annotationName))
                clazz = annotation.getClass();
        }
        return clazz;
    }

}