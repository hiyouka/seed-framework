package hiyouka.seedframework.util;


import hiyouka.seedframework.exception.BeanInstantiationException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class BeanUtils {



    public static <T> T instanceClass(Class<T> clazz) throws BeanInstantiationException {
        Assert.notNull(clazz, "Class must not be null");
        if (clazz.isInterface()) {
            throw new BeanInstantiationException( "Specified class is an interface", clazz.getName());
        }
        try {
            Constructor<T> ctor = clazz.getDeclaredConstructor();
            return instanceClass(ctor);
        }
        catch (NoSuchMethodException ex) {
            throw new BeanInstantiationException(clazz.getName(),"No default constructor found", ex);
        }
        catch (LinkageError err) {
            throw new BeanInstantiationException(clazz.getName(),"Unresolvable class definition", err);
        }
    }


    public static <T> T instanceClass(Constructor<T> ctor, Object... args){
        ReflectionUtils.makeAccessible(ctor);
        try {
            return ctor.newInstance(args);
        } catch (InstantiationException e) {
            throw new BeanInstantiationException(ctor, "Is it an abstract class?", e);
        } catch (IllegalAccessException e) {
            throw new BeanInstantiationException(ctor, "Is the constructor accessible?", e);
        } catch (InvocationTargetException e) {
            throw new BeanInstantiationException(ctor, "Constructor threw exception", e.getTargetException());
        }
    }

    public static <T> Constructor<T> findConstructor(Class<T> clazz, Object... args){
        Class<?>[] parameters = getClassFromArgs(args);
        try {
            return clazz.getDeclaredConstructor(parameters);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("No Such Constructor parameter ["+ Arrays.asList(parameters) +"] from class : " + clazz.getName(),e);
        }
    }

    public static Method findMethod(Class<?> clazz, String methodName, Object... args){
        Class<?>[] parameters = getClassFromArgs(args);
        try {
            return clazz.getDeclaredMethod(methodName,parameters);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("No Such Method : "+ methodName +" parameter ["+ Arrays.asList(parameters) +"] from class : " + clazz.getName(),e);
        }
    }

    private static Class<?>[] getClassFromArgs(Object... args){
        if(args == null)
            return null;
        Class<?>[] result = new Class[args.length];
        int i = 0;
        for(Object arg : args){
            result[i] = arg.getClass();
            i++;
        }
        return result;
    }
}