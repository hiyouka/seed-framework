package hiyouka.seedframework.beans.exception;

import hiyouka.seedframework.exception.SeedCoreException;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class BeansException extends SeedCoreException {

    private String beanName;

    public BeansException(String message) {
        super(message);
    }

    public BeansException(String message, Throwable e) {
        super(message,e);
    }
    public BeansException(String beanName, String message, Throwable e) {
        super(message,e);
        this.beanName = beanName;
    }


    public BeansException(String message, String beanName) {
        super(message);
        this.beanName = beanName;
    }

    public String getBeanName(){
        return this.beanName;
    }

}