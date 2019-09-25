package seed.seedframework.exception;

/**
 * @author hiyouka
 * Date: 2019/2/12
 * @since JDK 1.8
 */
public class FileReadException extends SeedCoreException{

    private final static String message = "read file error !! ";

    public FileReadException(){
        super(message);
    }

    public FileReadException(String message) {
        super(message);
    }

    public FileReadException(String message, Throwable e) {
        super(message, e);
    }
}