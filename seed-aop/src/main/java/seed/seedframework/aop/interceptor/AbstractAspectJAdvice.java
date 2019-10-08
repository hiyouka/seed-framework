package seed.seedframework.aop.interceptor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import seed.seedframework.aop.exception.AopExecuteException;
import seed.seedframework.aop.pointcut.AspectPointcut;
import seed.seedframework.aop.util.AspectJUtil;
import seed.seedframework.common.AnnotationAttributes;
import seed.seedframework.core.asm.ClassReaderUtils;
import seed.seedframework.core.intercept.Advice;
import seed.seedframework.core.intercept.MethodInterceptor;
import seed.seedframework.util.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * {@link MethodInterceptor}
 * @author hiyouka
 * @since JDK 1.8
 */
public abstract class AbstractAspectJAdvice implements Advice {

    private Log logger = LogFactory.getLog(AbstractAspectJAdvice.class);

    private Method aspectJMethod;

    private String methodName;

    private String[] aspectMethodParameterNames;

    private AspectPointcut aspectPointcut;

    private Class targetClass;

    private Object aspectJTarget;

    private PointCutArgumentName argumentName;


    public AbstractAspectJAdvice(Method aspectJMethod,Object aspectJTarget) {
        Assert.state(AspectJUtil.isAspectJMethod(aspectJMethod),"not support be not aspectJ method !!");
        this.aspectJMethod = aspectJMethod;
        this.methodName = aspectJMethod.getName();
        this.targetClass = aspectJMethod.getDeclaringClass();
        this.aspectPointcut = buildAspectPointcut();
        this.argumentName = initPointCutArgumentName();
        this.aspectJTarget = aspectJTarget;
        this.aspectMethodParameterNames = ClassReaderUtils.getParameterNamesByAsm5(this.targetClass,this.aspectJMethod);
    }

    protected AspectPointcut buildAspectPointcut(){
        AspectJUtil.AspectJMethodType aspectMethodType = getAspectMethodType();
        return new AspectPointcut(
                AspectJUtil.getAspectJMethodExpression(aspectMethodType,this.aspectJMethod),null);
    }

    protected abstract AspectJUtil.AspectJMethodType getAspectMethodType();

    protected Method getAspectJMethod() {
        return this.aspectJMethod;
    }

    protected String getMethodName() {
        return this.methodName;
    }

    public AspectPointcut getAspectPointcut() {
        return aspectPointcut;
    }

    public Class getTargetClass() {
        return targetClass;
    }



    protected Object invokeMethodWithArgs(Method method, Object returnVal, Throwable ex){
        Object result;
        if(this.aspectMethodParameterNames.length != 0){
            Class<?>[] parameterTypes = this.aspectJMethod.getParameterTypes();

            Object[] aspectArgs = new Object[parameterTypes.length];
            for(int i=0; i<parameterTypes.length; i++){
                if(JoinPoint.class.isAssignableFrom(parameterTypes[i])){
                    aspectArgs[i] = AopContext.currentMethodInvocation();
                }
                // set returning
                else if(this.aspectMethodParameterNames[i].equals(this.argumentName.returning)){
                    Type genericParameterType = this.aspectJMethod.getGenericParameterTypes()[i];
                    Type genericReturnType = method.getGenericReturnType();
                    if(!parameterTypes[i].equals(Object.class)  &&
                            !ResolverTypeUtil.isAssignableFrom(genericParameterType,genericReturnType)){

//                        throw new AopExecuteException("aop method can not bind argument : "
//                                + this.aspectMethodParameterNames[i] + ",cast type"+ parameterType.getName()
//                                + " to type " + returnVal.getClass().getName() + ", execute aop method : "
//                                + this.methodName);

                        // skip this method
                        logger.debug("aop method '"+ methodName +"' ignore process this method : " + method.getName()
                                + "case by returning type not accord with method return");
                        return returnVal;
                    }
                    else {
                        aspectArgs[i] = returnVal;
                    }
                }
                // set throwing
                else if(this.aspectMethodParameterNames[i].equals(this.argumentName.throwing)){
                    if(!parameterTypes[i].isAssignableFrom(ex.getClass())){
                        logger.debug("aop method '"+ methodName +"' ignore process this method : " + method.getName()
                                + "case by exception type not accord with method throws");
                        return returnVal;
                    }
                    else {
                        aspectArgs[i] = ex;
                    }
                }
                else {
                    throw new AopExecuteException("aop method can not bind argument : "+
                            this.aspectMethodParameterNames[i]);
                }

            }
            result = ReflectionUtils.invokeMethod(this.aspectJMethod,this.aspectJTarget,aspectArgs);
        }
        else {
            result = ReflectionUtils.invokeMethod(this.aspectJMethod,this.aspectJTarget);
        }

        return result;
    }


    private PointCutArgumentName initPointCutArgumentName(){
        AnnotationAttributes annotationAttributes = AnnotatedElementUtils.getAnnotationAttributes(this.aspectJMethod, getAnnotationType());
        if(annotationAttributes != null){
            String argNamesStr = annotationAttributes.getString("argNames");
            String[] argNames = StringUtils.notEmpty(argNamesStr) ? argNamesStr.split(",") : new String[0];
            String returning = (String) annotationAttributes.get("returning");
            String throwing = (String) annotationAttributes.get("throwing");
            return new PointCutArgumentName(argNames,throwing,returning);
        }
        else {
            return new PointCutArgumentName(null,null,null);
        }
    }

    protected abstract Class<? extends Annotation> getAnnotationType();

    protected static class PointCutArgumentName{

        private String[] argNames;

        private String throwing;

        private String returning;

        public PointCutArgumentName(String[] argNames, String throwing, String returning) {
            this.argNames = argNames;
            this.throwing = throwing;
            this.returning = returning;
        }

    }


}