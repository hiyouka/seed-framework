package hiyouka.seedframework.exception;

/**
 * @author hiyouka
 * Date: 2019/1/27
 * @since JDK 1.8
 */
public class BeanDefinitionStoreCoreException extends SeedCoreException {

    private final static String message = "BeanDefinition invalid !!";

    public BeanDefinitionStoreCoreException() {
        super(message);
    }

    public BeanDefinitionStoreCoreException(String message) {
        super(message);
    }

}