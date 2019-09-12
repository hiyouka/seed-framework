package seed.seedframework.beans.metadata;

import seed.seedframework.beans.annotation.Autowired;
import seed.seedframework.beans.annotation.Value;
import seed.seedframework.util.AnnotationUtils;

import java.lang.reflect.Field;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class DependencyDescriptor extends InjectionPoint {

    private final Class<?> declaringClass;

    private String methodName;

    private String attributeName;

    private final boolean required;

    private final DependencyType autowiredType;


    public DependencyDescriptor(MethodParameter parameter) {
        super(parameter);
        this.declaringClass = parameter.getDeclaringClass();
        Autowired annotation = parameter.getParameter().getAnnotation(Autowired.class);
        if(annotation == null){
            this.required = false;
        }
        else {
            this.required = (boolean) AnnotationUtils.getAttribute("required",annotation);
        }
        this.autowiredType = determineDependType();
        this.methodName = parameter.getMethod().getName();
    }

    public DependencyDescriptor(Field field,boolean required) {
        super(field);
        this.required = required;
        this.declaringClass = field.getDeclaringClass();
        this.autowiredType = determineDependType();
    }

    public DependencyDescriptor(MethodParameter parameter, boolean required) {
        super(parameter);
        this.declaringClass = parameter.getDeclaringClass();
        this.required = required;
        this.autowiredType = determineDependType();
        this.methodName = parameter.getMethod().getName();
    }


    private DependencyType determineDependType(){
        if(isField()){
            if(hasAnnotation(Autowired.class)){
                return DependencyType.AUTOWIRED;
            }
            return DependencyType.VALUE;
        }
        else{
            if(hasAnnotation(Value.class)){
                return DependencyType.VALUE;
            }
            return DependencyType.AUTOWIRED;
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

    public enum  DependencyType{
        AUTOWIRED,VALUE
    }

    public String getAttributeName() {
        if(attributeName == null){
            if(isField()){
                this.attributeName = this.field.getName();
            }
            else {
                this.attributeName = this.parameter.getParameterName();
            }
        }
        return attributeName;
    }
}