package hiyouka.seedframework.beans.factory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class InjectionPoint {

    protected Method method;

    protected Field field;

    protected Annotation[] fieldAnnotations;

    public InjectionPoint(Method method) {
        this.method = method;
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
        else {
            this.fieldAnnotations = method.getAnnotations();
        }

        return this.fieldAnnotations;
    }

    public Method getMethod() {
        return method;
    }

    public Field getField() {
        return field;
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
}