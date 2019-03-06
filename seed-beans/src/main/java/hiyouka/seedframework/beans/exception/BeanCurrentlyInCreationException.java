package hiyouka.seedframework.beans.exception;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class BeanCurrentlyInCreationException extends BeanCreatedException {

    public BeanCurrentlyInCreationException(String message) {
        super(message);
    }

    public BeanCurrentlyInCreationException(String message, Throwable e) {
        super(message, e);
    }

    public BeanCurrentlyInCreationException(String beanName, String message, Throwable e) {
        super(beanName, message, e);
    }

    public BeanCurrentlyInCreationException(String message, String beanName) {
        super(message, beanName);
    }
}