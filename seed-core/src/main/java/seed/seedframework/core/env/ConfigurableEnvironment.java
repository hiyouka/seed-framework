package seed.seedframework.core.env;

import seed.seedframework.core.io.resource.Resource;
import seed.seedframework.core.prop.PropertySources;

import java.util.Map;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public interface ConfigurableEnvironment extends Environment{

    /** 系统环境信息  */
    Map<String, Object> getSystemEnvironment();

    /** JVM信息 */
    Map<String, Object> getSystemProperties();

    PropertySources getPropertySources();

    void loadResource(Resource resource);



}