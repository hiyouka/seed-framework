package hiyouka.seedframework.core.env;

import hiyouka.seedframework.core.io.resource.Resource;
import hiyouka.seedframework.core.prop.MapPropertySource;
import hiyouka.seedframework.core.prop.ResourcePropertySource;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class StandardEnvironment extends AbstractEnvironment {

    public static final String SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME = "systemEnvironment";

    /** JVM system properties property source name: {@value} */
    public static final String SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME = "systemProperties";


    public StandardEnvironment(){
        this.initSystemProperty();
    }


    public void initSystemProperty(){
        this.propertySources.addProperty(new MapPropertySource(SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME,getSystemEnvironment()));
        this.propertySources.addProperty(new MapPropertySource(SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME,getSystemProperties()));
    }

    @Override
    public void loadResource(Resource resource) {
        this.propertySources.addProperty(new ResourcePropertySource(resource.getDescription(),resource));
    }


}