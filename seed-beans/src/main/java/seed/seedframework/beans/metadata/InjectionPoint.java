package seed.seedframework.beans.metadata;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class InjectionPoint {

    protected MethodParameter parameter;

    protected Field field;

    protected Annotation[] fieldAnnotations;

    public InjectionPoint(MethodParameter parameter) {
        this.parameter = parameter;
    }

    public InjectionPoint(Field field) {
        this.field = field;
    }

    public Annotation[] getFieldAnnotations(){
        if(this.fieldAnnotations != null){
            return this.fieldAnnotations;
        }
        if(field != null){
            this.fieldAnnotations  = field.getAnnotations();
        }
        else if(parameter != null){
            this.fieldAnnotations = parameter.getAnnotations();
        }

        return this.fieldAnnotations;
    }

    protected boolean isField(){
        return this.field != null;
    }

    public Field getField() {
        return field;
    }

    public MethodParameter getParameter() {
        return parameter;
    }

    public boolean hasAnnotation(Class<? extends Annotation> clazz){
        Annotation[] fieldAnnotations = getFieldAnnotations();
        for(Annotation ann : fieldAnnotations){
            if(ann.annotationType().equals(clazz)){
                return true;
            }
        }
        return false;
    }

    public Annotation getAnnotationForType(Class<? extends Annotation> clazz){
        Annotation[] fieldAnnotations = getFieldAnnotations();
        for(Annotation ann : fieldAnnotations){
            if(ann.annotationType().equals(clazz)){
                return ann;
            }
        }
        return null;
    }

    public Type getGenericType(){
        if(isField()){
            return this.field.getGenericType();
        }
        else {
            return this.parameter.getGenericParameterType();
        }
    }

    public Class<?> getType(){
        if(isField()){
            return this.field.getType();
        }
        else {
            return this.parameter.getParameterType();
        }
    }
}