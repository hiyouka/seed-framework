package seed.seedframework.aop.util;

import org.aspectj.lang.annotation.*;
import seed.seedframework.aop.interceptor.*;
import seed.seedframework.aop.pointcut.Pointcut;
import seed.seedframework.common.AnnotationAttributes;
import seed.seedframework.util.AnnotatedElementUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class AspectJUtil {

    private static final Class[] aspectJMethodAnnotations
            = new Class[]{Before.class,After.class, AfterReturning.class, AfterThrowing.class, Around.class};

    public static boolean isPointCutClass(Class<?> clazz){
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for(Method method : declaredMethods){
            if(AnnotatedElementUtils.isAnnotated(method, Pointcut.class.getName())){
                return true;
            }
        }
        return false;
    }

    public static boolean isAspectJMethod(Method method){
        if(method == null){
            return false;
        }
        for(Class clazz : aspectJMethodAnnotations){
            AnnotationAttributes attributes = AnnotatedElementUtils.getAnnotationAttributes(method, clazz.getName());
            if(attributes != null){
                return true;
            }
        }
        return false;
    }

    public static AnnotationAttributes getAspectMethodAttribute(Method method){
        for(Class clazz : aspectJMethodAnnotations){
            AnnotationAttributes attributes = AnnotatedElementUtils.getAnnotationAttributes(method, clazz.getName());
            if(attributes != null){
                return attributes;
            }
        }
        return null;
    }

    public static String getAspectJMethodExpression(AspectJMethodType type,Method method){
        String expression = null;
        switch (type){
            case BEFORE:
                expression = (String) AnnotatedElementUtils.getAttribute(method,Before.class,"value");
                break;
            case AFTER:
                expression = (String) AnnotatedElementUtils.getAttribute(method,After.class,"value");
                break;
            case AROUND:
                expression = (String) AnnotatedElementUtils.getAttribute(method,Around.class,"value");
                break;
            case AFTER_THROWING:
                expression = (String) AnnotatedElementUtils.getAttribute(method,AfterThrowing.class,"value");
                break;
            case AFTER_RETURNING:
                expression = (String) AnnotatedElementUtils.getAttribute(method,AfterReturning.class,"value");
                break;
        }
        return expression;
    }

    public enum AspectJMethodType{
        BEFORE,AFTER,AFTER_RETURNING,AFTER_THROWING,AROUND
    }

    public static AbstractAspectJAdvice initAdvice(Method method){
        AnnotationAttributes attributes = getAspectMethodAttribute(method);
        if(attributes != null){
            Class<? extends Annotation> aClass = attributes.annotationType();
            if(aClass.equals(Before.class)){
                return new MethodBeforeAspectJAdvice(method);
            }
            if(aClass.equals(After.class)){
                return new MethodAfterAspectJAdvice(method);
            }
            if(aClass.equals(AfterReturning.class)){
                return new MethodAfterAspectJAdvice(method);
            }
            if(aClass.equals(Around.class)){
                return new MethodAroundAspectJAdvice(method);
            }
            if(aClass.equals(AfterThrowing.class)){
                return new MethodThrowingAspectJAdvice(method);
            }
        }
        return null;
    }


}