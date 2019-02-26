package hiyouka.seedframework.io.resource;

import com.sun.istack.internal.Nullable;
import hiyouka.seedframework.util.ResourceUtils;

import java.io.IOException;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public interface ResourceLoader {

    String CLASSPATH_URL_PREFIX = ResourceUtils.CLASSPATH_URL_PREFIX;


    /**
     * 返回指定资源的Resource对象
     * @param location 资源路径
     * @return 资源路径的Resource对象
     * Date: 2019/2/25
     */
    Resource getResource(String location) throws IOException;

    @Nullable
    ClassLoader getClassLoader();
}