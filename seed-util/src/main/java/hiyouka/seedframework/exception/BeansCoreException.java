package hiyouka.seedframework.exception;

/**
 * @author hiyouka
 * Date: 2019/1/27
 * @since JDK 1.8
 */
public class BeansCoreException extends SeedCoreException {

    private final static String message = "get Bean error !!";

    public BeansCoreException() {
        super(message);
    }

    public BeansCoreException(String message) {
        super(message);
    }

}