package hiyouka.seedframework.beans.exception;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class NoUniqueBeanDefinitionException extends NoSuchBeanDefinitionException {

    public NoUniqueBeanDefinitionException(String message) {
        super(message);
    }

    public NoUniqueBeanDefinitionException(String message, String beanName) {
        super(message, beanName);
    }
}