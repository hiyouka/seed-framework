package hiyouka.seedframework.beans.metadata;

import hiyouka.seedframework.util.AnnotatedElementUtils;
import hiyouka.seedframework.util.MultiValueMap;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedHashSet;
import java.util.Map;
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
        return AnnotatedElementUtils.getMetaAnnotationTypes(getIntrospectedClass(),annotationName);
    }

    @Override
    public boolean hasAnnotation(String annotationName) {
        return isAnnotated(annotationName);
    }


    @Override
    public boolean hasAnnotatedMethods(String annotationName) {
        Method[] methods = getIntrospectedClass().getDeclaredMethods();
        for(Method method : methods){
            if(!method.isBridge() && method.getAnnotations().length > 0
                    && AnnotatedElementUtils.isAnnotated(method,annotationName)){
                return true;
            }
        }
        return false;
    }

    @Override
    public Set<MethodMetadata> getAnnotatedMethods(String annotationName) {
        Set<MethodMetadata> annotatedMethods = new LinkedHashSet<>(4);
        Method[] methods = getIntrospectedClass().getDeclaredMethods();
        for(Method method : methods){
            if (!method.isBridge() && method.getAnnotations().length > 0 &&
                    AnnotatedElementUtils.isAnnotated(method, annotationName)) {
                Type genericReturnType = method.getGenericReturnType();
                if(genericReturnType instanceof Class){
                    annotatedMethods.add(new StandardMethodMetadata(method));
                }
                else if(genericReturnType instanceof ParameterizedType){
                    Type[] types = ((ParameterizedType) genericReturnType).getActualTypeArguments();
                    annotatedMethods.add(new GenericMethodMetadata(method,types));
                }
            }
        }
        return annotatedMethods;
    }


    @Override
    public boolean isAnnotated(String annotationName) {
        return AnnotatedElementUtils.isAnnotated(getIntrospectedClass(),annotationName);
    }

    @Override
    public Map<String, Object> getAnnotationAttributes(String annotationName) {
        return (this.annotations.length > 0 ? AnnotatedElementUtils.getAnnotationAttributes(
                getIntrospectedClass(), annotationName) : null);
    }

    @Override
    public MultiValueMap<String, Object> getAllAnnotationAttributes(String annotationName) {
        return (this.annotations.length > 0 ? AnnotatedElementUtils.getAttributes(
                getIntrospectedClass(), annotationName) : null);
    }

}