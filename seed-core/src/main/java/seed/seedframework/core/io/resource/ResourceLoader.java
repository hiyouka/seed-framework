package seed.seedframework.core.io.resource;

import seed.seedframework.util.ResourceUtils;

import javax.annotation.Nullable;
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