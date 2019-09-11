package seed.seedframework.core.prop;

import javax.annotation.Nullable;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public interface PropertySources extends Iterable<PropertySource<?>>{


    @Nullable
    PropertySource<?> getProperty(String name);

    void addProperty(PropertySource propertySource);

    void removeProperty(PropertySource propertySource);

    boolean containProperty(String name);

}