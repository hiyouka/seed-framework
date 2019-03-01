package hiyouka.seedframework.util;


import hiyouka.seedframework.exception.BeanInstantiationException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

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

//    public static <T> T instantiateClass(Constructor<T> ctor, Object... args) throws BeanInstantiationException {
//        Assert.notNull(ctor, "Constructor must not be null");
//        try {
//            ReflectionUtils.makeAccessible(ctor);
//            return (KotlinDetector.isKotlinType(ctor.getDeclaringClass()) ?
//                    KotlinDelegate.instantiateClass(ctor, args) : ctor.newInstance(args));
//        }
//        catch (InstantiationException ex) {
//            throw new BeanInstantiationException(ctor, "Is it an abstract class?", ex);
//        }
//        catch (IllegalAccessException ex) {
//            throw new BeanInstantiationException(ctor, "Is the constructor accessible?", ex);
//        }
//        catch (IllegalArgumentException ex) {
//            throw new BeanInstantiationException(ctor, "Illegal arguments for constructor", ex);
//        }
//        catch (InvocationTargetException ex) {
//            throw new BeanInstantiationException(ctor, "Constructor threw exception", ex.getTargetException());
//        }
//    }
}