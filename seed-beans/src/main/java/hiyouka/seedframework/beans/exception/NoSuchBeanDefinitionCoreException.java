package hiyouka.seedframework.beans.exception;

import hiyouka.seedframework.exception.SeedCoreException;

/**
 * @author hiyouka
 * Date: 2019/1/27
 * @since JDK 1.8
 */
public class NoSuchBeanDefinitionCoreException extends SeedCoreException {

    private final static String message = "not find BeanDefinition error !!";

    public NoSuchBeanDefinitionCoreException() {
        super(message);
    }

    public NoSuchBeanDefinitionCoreException(String message) {
        super(message);
    }

}