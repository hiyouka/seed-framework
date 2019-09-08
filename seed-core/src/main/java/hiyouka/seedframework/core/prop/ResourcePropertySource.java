package hiyouka.seedframework.core.prop;

import hiyouka.seedframework.core.io.resource.Resource;
import hiyouka.seedframework.util.PropertiesLoadUtils;

import java.util.Map;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class ResourcePropertySource extends MapPropertySource{

    private String resourceName;

    public ResourcePropertySource(String name, Resource resource) {
        super(name, PropertiesLoadUtils.loadProperties(resource));
        this.resourceName = resource.getDescription();
    }


    public ResourcePropertySource(String name, String resourceName, Map<String,Object> prop) {
        super(name, prop);
        this.resourceName = resourceName;
    }

    public String getResourceName() {
        return resourceName;
    }
}