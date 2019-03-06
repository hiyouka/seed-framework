package hiyouka.seedframework.beans.exception;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class BeanNotRequiredException extends BeansException {

    public BeanNotRequiredException(String message) {
        super(message);
    }

    public BeanNotRequiredException(String message, Throwable e) {
        super(message, e);
    }

    public BeanNotRequiredException(String beanName, String message, Throwable e) {
        super(beanName, message, e);
    }

    public BeanNotRequiredException(String message, String beanName) {
        super(message, beanName);
    }
}