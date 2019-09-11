package seed.seedframework.exception;

/**
 * @author hiyouka
 * Date: 2019/2/12
 * @since JDK 1.8
 */
public class SeedCoreException extends RuntimeException {

    public SeedCoreException(String message) {
        super(message);
    }

    public SeedCoreException(String message, Throwable e){
        super(message,e);
    }

}