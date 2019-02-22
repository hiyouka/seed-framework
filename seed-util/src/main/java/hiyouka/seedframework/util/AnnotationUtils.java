package hiyouka.seedframework.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hiyouka
 * Date: 2019/2/21
 * @since JDK 1.8
 */
public class AnnotationUtils {

    private static final Map<Class<? extends Annotation>, List<Method>> attributeMethodsCache =
            new ConcurrentHashMap<>(256);

    public static boolean isInJavaLangAnnotationPackage(Annotation annotation) {
        return (annotation != null && isInJavaLangAnnotationPackage(annotation.annotationType().getName()));
    }

    public static boolean isInJavaLangAnnotationPackage(String annotationType) {
        return (annotationType != null && annotationType.startsWith("java.lang.annotation"));
    }


    public static Map<String,Object> getAttributes(Annotation annotated){
        List<Method> attributeMethods = getAttributeMethods(annotated.getClass());
        Map<String,Object> result = new LinkedHashMap<>();
        for(Method method : attributeMethods){
            result.put(method.getName(),ReflectionUtils.invokeMethod(method,annotated));
        }
        return result;
    }

    public static Object getAttribute(String attributeName, Annotation annotated) {
        List<Method> attributeMethods = getAttributeMethods(annotated.getClass());
        Method method;
        method = attributeMethods.stream()
                .filter(it -> it.getName().equals(attributeName))
                .findFirst()
                .orElse(null);
        if(method == null)
            throw new IllegalStateException("Method not found: " + attributeName);
        return ReflectionUtils.invokeMethod(method,annotated);
    }

    public static List<Method> getAttributeMethods(Class<? extends Annotation> annotationType) {
        List<Method> methods = attributeMethodsCache.get(annotationType);
        if (methods != null) {
            return methods;
        }
        methods = new ArrayList<>();
        for (Method method : annotationType.getDeclaredMethods()) {
            if (isAttributeMethod(method)) {
                ReflectionUtils.makeAccessible(method);
                methods.add(method);
            }
        }
        attributeMethodsCache.put(annotationType, methods);
        return methods;
    }


    static boolean isAttributeMethod(Method method) {
        return (method != null && method.getParameterTypes().length == 0 && method.getReturnType() != void.class
                && !isAnnotationTypeMethod(method) && !ObjectUtils.isHashCodeMethod(method) && !ObjectUtils.isToStringMethod(method));
    }


    static boolean isAnnotationTypeMethod(Method method) {
        return (method != null && method.getName().equals("annotationType") && method.getParameterTypes().length == 0);
    }



}