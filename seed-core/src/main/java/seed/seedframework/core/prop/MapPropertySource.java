package seed.seedframework.core.prop;

import java.util.Map;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class MapPropertySource extends PropertySource<Map<String,Object>> {

    public MapPropertySource(String name, Map<String, Object> source) {
        super(name, source);
    }

    @Override
    public boolean containsProperty(String name) {
        return this.source.containsKey(name);
    }

    @Override
    public Object getProperty(String name) {
        return this.source.get(name);
    }
}