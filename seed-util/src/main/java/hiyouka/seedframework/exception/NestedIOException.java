package hiyouka.seedframework.exception;


import javax.annotation.Nullable;
import java.io.IOException;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class NestedIOException extends IOException {

    public NestedIOException(String msg) {
        super(msg);
    }

    public NestedIOException(@Nullable String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }

}