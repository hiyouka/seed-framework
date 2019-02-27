package hiyouka.seedframework.exception;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class BeansException extends SeedCoreException {

    private String beanName;

    public BeansException(String message) {
        super(message);
    }

    public BeansException(String message, String beanName) {
        super(message);
        this.beanName = beanName;
    }

    public String getBeanName(){
        return this.beanName;
    }

}