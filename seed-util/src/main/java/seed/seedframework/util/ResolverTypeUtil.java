package seed.seedframework.util;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 分析类是否符合该泛型属性
 * @author hiyouka
 * @since JDK 1.8
 */
public class ResolverTypeUtil {

    private static final Map<Type, Map<TypeVariable,Class>> typeCache = new ConcurrentHashMap<>();

    private static final Map<Type,Map<Class,Class[]>> typeGenericsCache = new ConcurrentHashMap<>();

    public static boolean isAssignableFrom(Type origin, Type target){
        if(target instanceof Class){
            return isAssignableFrom(origin,(Class)target);
        }
        else if(origin instanceof Class){
            Type rawType = ((ParameterizedType) target).getRawType();
            if(rawType instanceof Class){
                return ((Class<?>) origin).isAssignableFrom((Class) rawType);
            }
        }
        else if(origin instanceof ParameterizedType &&
                target instanceof ParameterizedType){
            Type originRawType = ((ParameterizedType) origin).getRawType();
            Type targetRawType = ((ParameterizedType) target).getRawType();
            if(!((Class<?>)originRawType).isAssignableFrom((Class<?>) targetRawType)){
                return false;
            }
            Map<Class, Class[]> classMap = obtainRealTypeForType(target);
            Class[] classes = classMap.get(originRawType);
            Type[] actualTypeArguments = ((ParameterizedType) origin).getActualTypeArguments();
            for(int i=0; i<actualTypeArguments.length; i++){
                if(!actualTypeArguments[i].equals(classes[i])){
                    return false;
                }
            }
            return true;
        }
        return false;
    }


    /**
     * 判断某个clazz是否源于某个属性(包含泛型判断)
     * @param element 属性
     * @param clazz 判断类
     * @return boolean
     */
    public static boolean isAssignableFrom(Type element, Class clazz){
        if(element instanceof Class){
            return ((Class<?>) element).isAssignableFrom(clazz);
        }
        else if(element instanceof ParameterizedType) {
            Type rawType = ((ParameterizedType) element).getRawType();
            if(rawType instanceof Class){
                if(!((Class<?>) rawType).isAssignableFrom(clazz)){
                    return false;
                }
                Map<Class, Class[]> classMap = obtainRealTypeForType(clazz);
                Type[] actualTypeArguments = ((ParameterizedType) element).getActualTypeArguments();
                Class[] classes = classMap.get(rawType);
                if(classes == null || classes.length == 0){
                    if(actualTypeArguments == null || actualTypeArguments.length == 0){
                        return true;
                    }
                    else {
                        return false;
                    }
                }
                for(int i=0; i<actualTypeArguments.length; i++){
                    if(!actualTypeArguments[i].equals(classes[i])){
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    public static Map<Class, Class[]> obtainRealTypeForType(Type type){
        Map<Class, Class[]> classMap = typeGenericsCache.get(type);
        if(classMap == null){
            foreachGenericTypeForType(type);
            classMap = typeGenericsCache.get(type);
        }
        return classMap;
    }

    private static void foreachGenericTypeForType(Type originType){
        if(originType instanceof Class){
            foreachGenericTypeForType(originType, (Class) originType);
        }
        else if(originType instanceof ParameterizedType){
            Type rawType = ((ParameterizedType) originType).getRawType();
            Map<Class, Class[]> classMap = typeGenericsCache.computeIfAbsent(originType, k -> new HashMap<>());
            Type[] actualTypeArguments = ((ParameterizedType) originType).getActualTypeArguments();
            Class[] classes = new Class[actualTypeArguments.length];
            obtainGenericsForType(originType);
            for(int i=0; i<actualTypeArguments.length; i++){
                if(actualTypeArguments[i] instanceof Class){
                    classes[i] = (Class) actualTypeArguments[i];
                }
                else if(actualTypeArguments[i] instanceof TypeVariable){
                    Map<TypeVariable, Class> mapper = obtainGenericsForType(originType);
                    classes[i] = mapper.get(actualTypeArguments[i]);
                }
            }
            classMap.put((Class) rawType,classes);
            foreachGenericTypeForType(originType, (Class) rawType);
        }
        else {
        }
    }

    private static void foreachGenericTypeForType(Type originType, Class match){
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
                Map<Class, Class[]> classMap = typeGenericsCache.computeIfAbsent(originType, k -> new HashMap<>());
                Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
                Class[] classes = new Class[actualTypeArguments.length];
                for(int i=0; i<actualTypeArguments.length; i++){
                    if(actualTypeArguments[i] instanceof Class){
                        classes[i] = (Class) actualTypeArguments[i];
                    }
                    else if(actualTypeArguments[i] instanceof TypeVariable){
                        Map<TypeVariable, Class> mapper = obtainGenericsForType(originType);
                        TypeVariable actualTypeArgument = (TypeVariable) actualTypeArguments[i];
                        Class realType = mapper.get(actualTypeArgument);
                        if(realType == null){
                            GenericDeclaration genericDeclaration = actualTypeArgument.getGenericDeclaration();
                            TypeVariable<?>[] typeParameters = genericDeclaration.getTypeParameters();
                            Class[] superClasses = classMap.get(genericDeclaration);
                            if(superClasses != null && superClasses.length == typeParameters.length){
                                for(int j=0; j<typeParameters.length; j++){
                                    if(typeParameters[j].equals(actualTypeArgument)){
                                        realType = superClasses[j];
                                    }
                                }
                            }
                        }
                        classes[i] = realType;
                    }
                }
                classMap.put((Class) rawType,classes);
                foreachGenericTypeForType(originType, (Class) rawType);
            }
            else if(type instanceof Class){
                Map<Class, Class[]> classMap = typeGenericsCache.computeIfAbsent(originType, k -> new HashMap<>());
                classMap.put((Class) type,null);
            }
        }
    }

    public static Map<TypeVariable,Class> obtainGenericsForType(Type type){
        Map<TypeVariable, Class> typeVariableClassMap = typeCache.get(type);
        if(typeVariableClassMap == null){
            Map<TypeVariable, Class> map = resolveGenerics(type, null);
            typeCache.put(type,map);
            return map;
        }
        return typeVariableClassMap;
    }

    private static Map<TypeVariable,Class> resolveGenerics(Type type, Map<TypeVariable,Class> mapper){
        if(mapper == null)
            mapper = new HashMap<>();
        if(type instanceof Class){
            Type genericSuperclass = ((Class) type).getGenericSuperclass();
            pullGenerics(genericSuperclass,mapper);
            Type[] genericInterfaces = ((Class) type).getGenericInterfaces();
            for(Type t : genericInterfaces){
                pullGenerics(t,mapper);
            }
        }
        else {
            pullGenerics(type,mapper);
        }
        return mapper;
    }

    private static  void pullGenerics(Type type,Map<TypeVariable,Class> mapper){
        if(type instanceof ParameterizedType){
            Type rawType = ((ParameterizedType) type).getRawType();
            if(rawType instanceof Class){
                resolveGenerics(rawType,mapper);
                TypeVariable[] typeParameters = ((Class) rawType).getTypeParameters();
                Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
                for(int i=0; i<typeParameters.length; i++){
                    if(actualTypeArguments[i] instanceof Class){
                        // cache
                        mapper.put(typeParameters[i], (Class<?>) actualTypeArguments[i]);
                    }
                }
            }
        }
        // 无泛型类
        else if(type instanceof Class){

        }
    }

    public static void clearCache(){
        typeCache.clear();
        typeGenericsCache.clear();
    }

}