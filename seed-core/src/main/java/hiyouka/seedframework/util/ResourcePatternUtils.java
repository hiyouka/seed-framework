package hiyouka.seedframework.util;

import hiyouka.seedframework.core.io.resource.PathMatchingResourcePatternResolver;
import hiyouka.seedframework.core.io.resource.ResourceLoader;
import hiyouka.seedframework.core.io.resource.ResourcePatternResolver;
import javax.annotation.Nullable;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class ResourcePatternUtils {

    public static ResourcePatternResolver getResourcePatternResolver(@Nullable ResourceLoader resourceLoader) {
        if (resourceLoader instanceof ResourcePatternResolver) {
            return (ResourcePatternResolver) resourceLoader;
        }
        else if (resourceLoader != null) {
            return new PathMatchingResourcePatternResolver(resourceLoader);
        }
        else {
            return new PathMatchingResourcePatternResolver();
        }
    }



}