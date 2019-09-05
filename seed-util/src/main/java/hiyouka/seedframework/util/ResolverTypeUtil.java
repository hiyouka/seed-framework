package hiyouka.seedframework.util;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 分析类是否符合该泛型属性
 * @author hiyouka
 * @since JDK 1.8
 */
public class ResolverTypeUtil {

    private static final Map<Class, Map<TypeVariable,Class>> typeCache = new ConcurrentHashMap<>();

    private static final Map<Class,Map<Class,Class[]>> genericsCache = new ConcurrentHashMap<>();

    /**
     * 判断某个clazz是否源于某个属性(包含泛型判断)
     * @param element 属性
     * @param clazz 判断类
     * @return boolean
     */
    public static boolean isAssignableFrom(AnnotatedElement element, Class clazz){
        if(element instanceof Class){
            return element.equals(clazz);
        }
        else if(element instanceof Field){
            Type genericType = ((Field) element).getGenericType();
            if(genericType instanceof ParameterizedType) {
                Type rawType = ((ParameterizedType) genericType).getRawType();
                if(rawType instanceof Class){
                    if(!((Class) rawType).isAssignableFrom(clazz)){
                        return false;
                    }
                    Type[] actualTypeArguments = ((ParameterizedType) genericType).getActualTypeArguments();
                    Map<Class, Class[]> classMap = obtainRealTypeForClass(clazz);
                    Class[] classes = classMap.get(rawType);
                    for(int i=0; i<actualTypeArguments.length; i++){
                        if(!actualTypeArguments[i].equals(classes[i])){
                            return false;
                        }
                    }
                    return true;
                }
            }
            else if(genericType instanceof Class){
                return ((Class) genericType).isAssignableFrom(clazz);
            }
        }
        return false;
    }

    public static boolean fieldIsMatchOfGenerics(Field field,Type[] generics){
        Type genericType = field.getGenericType();
        if(genericType instanceof Class){
            return false;
        }
        else if(genericType instanceof ParameterizedType){
            Type[] types = ((ParameterizedType) genericType).getActualTypeArguments();
            if(types.length != generics.length){
                return false;
            }
            for(int i=0; i< types.length; i++){
                if(!types[i].equals(generics[i])){
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static Map<Class, Class[]> obtainRealTypeForClass(Class clazz){
        Map<Class, Class[]> classMap = genericsCache.get(clazz);
        if(classMap == null){
            foreachGenericType(clazz,clazz);
            classMap = genericsCache.get(clazz);
        }
        return classMap;
    }


    private static void foreachGenericType(Class originClass, Class match){
        Type genericSuperclass = match.getGenericSuperclass();
        Type[] genericInterfaces = match.getGenericInterfaces();
        List<Type> types = new ArrayList<>(genericInterfaces.length + 1);
        if(genericInterfaces.length != 0){
            types.addAll(Arrays.asList(genericInterfaces));
        }
        if(genericSuperclass != null){
            types.add(genericSuperclass);
        }
        for(Type type : types){
            if(type instanceof ParameterizedType){
                Type rawType = ((ParameterizedType) type).getRawType();
                Map<Class, Class[]> classMap = genericsCache.computeIfAbsent(originClass, k -> new HashMap<>());
                Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
                Class[] classes = new Class[actualTypeArguments.length];
                for(int i=0; i<actualTypeArguments.length; i++){
                    if(actualTypeArguments[i] instanceof Class){
                        classes[i] = (Class) actualTypeArguments[i];
                    }
                    else if(actualTypeArguments[i] instanceof TypeVariable){
                        Map<TypeVariable, Class> mapper = obtainGenericsForClass(originClass);
                        classes[i] = mapper.get(actualTypeArguments[i]);
                    }
                }
                classMap.put((Class) rawType,classes);
                foreachGenericType(originClass, (Class) rawType);
            }
            else if(type instanceof Class){
                Map<Class, Class[]> classMap = genericsCache.computeIfAbsent(originClass, k -> new HashMap<>());
                classMap.put((Class) type,null);
            }
        }
    }


    public static Map<TypeVariable,Class> obtainGenericsForClass(Class clazz){
        Map<TypeVariable, Class> typeVariableClassMap = typeCache.get(clazz);
        if(typeVariableClassMap == null){
            return resolveGenerics(clazz,null);
        }
        return typeVariableClassMap;
    }

    private static Map<TypeVariable,Class> resolveGenerics(Class clazz, Map<TypeVariable,Class> mapper){
        if(mapper == null)
            mapper = new HashMap<>();
        Type genericSuperclass = clazz.getGenericSuperclass();
        pullGenerics(genericSuperclass,mapper);
        Type[] genericInterfaces = clazz.getGenericInterfaces();
        for(Type type : genericInterfaces){
            pullGenerics(type,mapper);
        }
        return mapper;
    }

    private static  void pullGenerics(Type type,Map<TypeVariable,Class> mapper){
        if(type instanceof ParameterizedType){
            Type rawType = ((ParameterizedType) type).getRawType();
            if(rawType instanceof Class){
                resolveGenerics((Class) rawType,mapper);
                TypeVariable[] typeParameters = ((Class) rawType).getTypeParameters();
                Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
                for(int i=0; i<typeParameters.length; i++){
                    if(actualTypeArguments[i] instanceof Class)
                        mapper.put(typeParameters[i], (Class<?>) actualTypeArguments[i]);
                }
            }
        }
        // 无泛型类
        else if(type instanceof Class){

        }
    }

    public static void clearCache(){
        typeCache.clear();
        genericsCache.clear();
    }

}