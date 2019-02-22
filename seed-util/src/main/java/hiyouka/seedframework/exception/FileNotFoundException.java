package hiyouka.seedframework.exception;

/**
 * @author hiyouka
 * Date: 2019/2/12
 * @since JDK 1.8
 */
public class FileNotFoundException extends SeedCoreException {

    private static final String message = "no file get from this path";

    public FileNotFoundException(){super(message);};

    public FileNotFoundException(String message) {
        super(message);
    }
}