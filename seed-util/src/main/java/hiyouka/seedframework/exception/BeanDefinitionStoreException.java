package hiyouka.seedframework.exception;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class BeanDefinitionStoreException extends SeedCoreException {

    private String beanName;

    public BeanDefinitionStoreException(String message) {
        super(message);
    }

    public BeanDefinitionStoreException(String message, Exception e) {
        super(message, e);
    }

    public BeanDefinitionStoreException(String beanName, String msg) {
        super(msg);
        this.beanName = beanName;
    }




    public String getBeanName(){
        return this.beanName;
    }
}