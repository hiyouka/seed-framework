package seed.seedframework.core.io.resource;

import java.io.IOException;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public interface ResourcePatternResolver extends ResourceLoader{

    /**
     * 使用classpath:* 可读取包含引入jar包在内的指定文件(扫描解析jar包)
     */
    String CLASSPATH_ALL_URL_PREFIX = "classpath*:";

    /**
     * 将指定路径的所有资源解析为Resource
     * @param locationPattern 文件路径
     * @return 相应的Resource文件
     */
    Resource[] getResources(String locationPattern) throws IOException;

}