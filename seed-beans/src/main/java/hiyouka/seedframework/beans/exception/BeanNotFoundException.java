package hiyouka.seedframework.beans.exception;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class BeanNotFoundException extends BeansException {

    public BeanNotFoundException(String message) {
        super(message);
    }

    public BeanNotFoundException(String message, Throwable e) {
        super(message, e);
    }

    public BeanNotFoundException(String beanName, String message, Throwable e) {
        super(beanName, message, e);
    }

    public BeanNotFoundException(String message, String beanName) {
        super(message, beanName);
    }
}