package hiyouka.seedframework.core.io.resource;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public interface WritableResource extends Resource {

    /**
     * 该资源是否可以写入
     */
    default boolean isWritable() {
        return true;
    }

    OutputStream getOutputStream() throws IOException;

}