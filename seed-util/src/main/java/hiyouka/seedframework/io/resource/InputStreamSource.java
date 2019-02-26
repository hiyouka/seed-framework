package hiyouka.seedframework.io.resource;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public interface InputStreamSource {

    InputStream getInputStream() throws IOException;

}