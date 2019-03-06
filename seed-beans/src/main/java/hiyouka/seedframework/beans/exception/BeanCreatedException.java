package hiyouka.seedframework.beans.exception;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class BeanCreatedException extends BeansException {

    public BeanCreatedException(String message) {
        super(message);
    }

    public BeanCreatedException(String message, Throwable e) {
        super(message, e);
    }

    public BeanCreatedException(String beanName, String message, Throwable e) {
        super(beanName, message, e);
    }

    public BeanCreatedException(String message, String beanName) {
        super(message, beanName);
    }
}