package hiyouka.seedframework.beans.factory;

import hiyouka.seedframework.beans.annotation.Autowired;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class DependencyDescriptor extends InjectionPoint {

    private final Class<?> declaringClass;

    private String methodName;

    private final boolean required;

    private final DependencyType autowiredType;

    public DependencyDescriptor(Field field,boolean required) {
        super(field);
        this.required = required;
        this.declaringClass = field.getDeclaringClass();
        this.autowiredType = determineDependType();
    }

    public DependencyDescriptor(Method method, boolean required) {
        super(method);
        this.declaringClass = this.method.getDeclaringClass();
        this.required = required;
        this.autowiredType = determineDependType();
        this.methodName = method.getName();
    }

    private DependencyType determineDependType(){
        if(this.field != null){
            Annotation[] fieldAnnotations = this.fieldAnnotations;
            if(hasAnnotation(Autowired.class)){
                return DependencyType.AUTOWIRED;
            }
            return DependencyType.VALUE;
        }
        else {
            return DependencyType.VALUE;
        }
    }

    public Class<?> getDeclaringClass() {
        return declaringClass;
    }

    public String getMethodName() {
        return methodName;
    }

    public boolean isRequired() {
        return required;
    }

    public DependencyType getAutowiredType() {
        return autowiredType;
    }

    enum  DependencyType{
        AUTOWIRED,VALUE
    }
}