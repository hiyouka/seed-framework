package seed.seedframework.beans.metadata;

import seed.seedframework.core.asm.ClassReaderUtils;
import seed.seedframework.util.Assert;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class MethodParameter {

    private final Executable executable;

    private final int parameterIndex;

    private final Class<?> declaringClass;

    private volatile Parameter parameter;

    private volatile Annotation[] annotations;

    private volatile Class<?> parameterType;

    private volatile Type genericParameterType;

    private volatile String parameterName;

    private volatile String[] parameterNames;

    private final String methodName;

    /**
     *  -1 表示returenType
     */
    public MethodParameter(Method method, int parameterIndex){
        this.executable = method;
        int parameterCount = method.getParameterCount();
        if((parameterIndex < -1) || (parameterIndex > (parameterCount-1))){
            Assert.state(false,"can find parameterCount:" + parameterCount +" parameter");
        }
        this.parameterIndex = parameterIndex;
        this.declaringClass = method.getDeclaringClass();
        this.methodName = method.getName();
    }

    public MethodParameter(Constructor constructor, int parameterIndex) {
        this.executable = constructor;
        int parameterCount = constructor.getParameterCount();
        if((parameterIndex < -1) || (parameterIndex > (parameterCount-1))){
            Assert.state(false,"can find parameterCount:" + parameterCount +" parameter");
        }
        this.parameterIndex = parameterIndex;
        this.declaringClass = constructor.getDeclaringClass();
        this.methodName = constructor.getName();
    }

    public String getMethodName() {
        return methodName;
    }

    public Executable getExecutable() {
        return executable;
    }

    public int getParameterIndex() {
        return parameterIndex;
    }

    public Class<?> getDeclaringClass() {
        return declaringClass;
    }

    public Parameter getParameter() {
        if(this.parameter == null && this.parameterIndex != -1){
            parameter = this.executable.getParameters()[this.parameterIndex];
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
                if(this.executable instanceof Method){
                    parameterType = ((Method)this.executable).getReturnType();
                }
                else if(this.executable instanceof Constructor){
                    parameterType = this.executable.getDeclaringClass();
                }
            }
            else {
                if(this.executable instanceof Method){
                    parameterType = this.parameter.getType();
                }
                else if(this.executable instanceof Constructor){
                    Type parameterizedType = this.parameter.getParameterizedType();
                    if(parameterizedType instanceof Class){
                        parameterType = (Class<?>)parameterizedType;
                    }
                    else if(parameterizedType instanceof ParameterizedType){
                        parameterType = (Class<?>) ((ParameterizedType) parameterizedType).getRawType();
                    }
                }
            }
        }
        return parameterType;
    }

    public Type getGenericParameterType() {
        if(this.genericParameterType == null){
            if(parameterIndex < 0){
                if(this.executable instanceof Method){
                    genericParameterType = ((Method)this.executable).getGenericReturnType();
                }
                else if(this.executable instanceof Constructor){
                    genericParameterType = ((Constructor)this.executable).getDeclaringClass();
                }
            }
            else {
                genericParameterType = this.executable.getGenericParameterTypes()[parameterIndex];
            }
        }
        return genericParameterType;
    }

    public String getParameterName() {
        if(parameterIndex > -1 && this.parameterName == null){
            if(this.parameterNames == null){
                    parameterNames = ClassReaderUtils.getParameterNamesByAsm5(this.getDeclaringClass(),this.getExecutable());
                }
            parameterName = parameterNames[parameterIndex];
        }
        return parameterName;
    }
}