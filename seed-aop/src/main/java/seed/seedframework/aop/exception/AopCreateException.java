package seed.seedframework.aop.exception;

import seed.seedframework.exception.SeedCoreException;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class AopCreateException extends SeedCoreException {

    private Class<?> targetClass;

    private final static String message = "aop proxy bean creator error";

    public AopCreateException(){
        super(message);
    }

    public AopCreateException(String message) {
        super(message);
    }

    public AopCreateException(String message, Throwable e) {
        super(message, e);
    }

    public void setTargetClass(Class<?> targetClass) {
        this.targetClass = targetClass;
    }
}