package hiyouka.seedframework.exception;

/**
 * @author hiyouka
 * Date: 2019/2/12
 * @since JDK 1.8
 */
public class FileSizeOutException extends SeedCoreException {

    private static final String message = " file size out of max size !!";

    public FileSizeOutException(String message) {
        super(message);
    }

    public FileSizeOutException() {
        super(message);
    }
}