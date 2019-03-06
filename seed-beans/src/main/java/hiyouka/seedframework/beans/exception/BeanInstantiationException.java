package hiyouka.seedframework.beans.exception;

import java.lang.reflect.Constructor;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class BeanInstantiationException extends BeansException {

    private Constructor<?> ctor;

    public BeanInstantiationException(String message) {
        super(message);
    }

    public BeanInstantiationException(String message, String beanName) {
        super(message, beanName);
    }

    public BeanInstantiationException(String beanName, String message, Throwable e) {
        super(beanName,message,e);
    }

    public BeanInstantiationException(Constructor<?> ctor, String message, Throwable e) {
        super("Failed to instantiate [" + ctor.getDeclaringClass().getName() + "]: " + message,e);
    }


    public BeanInstantiationException(Constructor<?> ctor,String message, String beanName) {
        super("Failed to instantiate [" + ctor.getDeclaringClass().getName() + "]: " + message,beanName);
    }

    public Constructor<?> getCtor() {
        return ctor;
    }
}