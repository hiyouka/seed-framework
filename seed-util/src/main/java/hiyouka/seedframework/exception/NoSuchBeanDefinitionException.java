package hiyouka.seedframework.exception;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class NoSuchBeanDefinitionException extends BeansException {


    public NoSuchBeanDefinitionException(String message) {
        super(message);
    }

    public NoSuchBeanDefinitionException( String msg, String beanName) {
        super(msg, beanName);
    }

    public String getBeanName(){
        return super.getBeanName();
    }

}