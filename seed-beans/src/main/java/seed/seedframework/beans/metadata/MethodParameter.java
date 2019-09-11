package seed.seedframework.beans.metadata;

import seed.seedframework.util.Assert;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class MethodParameter {

    private final Method method;

    private final int parameterIndex;

    private final Class<?> declaringClass;

    private volatile Parameter parameter;

    private volatile Annotation[] annotations;

    private volatile Class<?> parameterType;

    private volatile Type genericParameterType;

    private volatile String parameterName;

    /**
     *  -1 表示returenType
     */
    public MethodParameter(Method method, int parameterIndex){
        this.method = method;
        int parameterCount = method.getParameterCount();
        if((parameterIndex < -1) || (parameterIndex > (parameterCount-1))){
            Assert.state(false,"can find parameterCount:" + parameterCount +" parameter");
        }
        this.parameterIndex = parameterIndex;
        this.declaringClass = method.getDeclaringClass();
    }

    public Method getMethod() {
        return method;
    }

    public int getParameterIndex() {
        return parameterIndex;
    }

    public Class<?> getDeclaringClass() {
        return declaringClass;
    }

    public Parameter getParameter() {
        if(this.parameter == null && this.parameterIndex != -1){
            parameter = this.method.getParameters()[this.parameterIndex];
        }
        return parameter;
    }

    public Annotation[] getAnnotations() {
        if(this.annotations == null){
            annotations = this.parameter.getAnnotations();
        }
        return annotations;
    }

    public Class<?> getParameterType() {
        if(this.parameterType == null){
            if(parameterIndex < 0){
                parameterType = method.getReturnType();
            }
            else {
                parameterType = this.parameter.getType();
            }
        }
        return parameterType;
    }

    public Type getGenericParameterType() {
        if(this.genericParameterType == null){
            if(parameterIndex < 0){
                genericParameterType = this.method.getGenericReturnType();
            }
            else {
                genericParameterType = this.method.getGenericParameterTypes()[parameterIndex];
            }
        }
        return genericParameterType;
    }

    public String getParameterName() {
        if(parameterIndex > 0 && this.getParameterName() == null){
            parameterName = getParameter().getName();
        }
        return parameterName;
    }
}