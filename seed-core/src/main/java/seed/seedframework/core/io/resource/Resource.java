package seed.seedframework.core.io.resource;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public interface Resource extends InputStreamSource {

    boolean exists();

    /**
     * 该资源是否可读取
     */
    default boolean isReadable(){
        return false;
    }

    /**
     * 流是否打开
     */
    default boolean isOpen(){
        return false;
    }

    default boolean isFile(){
        return false;
    }

    URL getURL() throws IOException;

    URI getURI() throws IOException;

    File getFile() throws IOException;

    /**
     *  内容长度
     */
    long contentLength() throws IOException;

    String getFilename();

    /**
     * 文件描述
     */
    String getDescription();


    /**
     * 创建此资源的相对资源
     * @param relativePath 相对路径
     * @return 返回资源对象
     * @throws IOException 如果资源无法确定
     */
    Resource createRelative(String relativePath) throws IOException;
}