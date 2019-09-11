package seed.seedframework.core.prop;

import javax.annotation.Nullable;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public interface PropertyResolver {

    boolean containsProperty(String key);

    @Nullable
    String getProperty(String key);

    @Nullable
    <T> T getProperty(String key, Class<T> requireType);


}