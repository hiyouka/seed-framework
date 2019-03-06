package hiyouka.seedframework.beans.exception;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class NoUniqueBeanException extends BeansException {

    public NoUniqueBeanException(String message) {
        super(message);
    }

    public NoUniqueBeanException(String message, Throwable e) {
        super(message, e);
    }

    public NoUniqueBeanException(String beanName, String message, Throwable e) {
        super(beanName, message, e);
    }

    public NoUniqueBeanException(String message, String beanName) {
        super(message, beanName);
    }
}