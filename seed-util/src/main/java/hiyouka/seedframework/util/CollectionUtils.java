package hiyouka.seedframework.util;

import com.sun.istack.internal.Nullable;

import java.util.Collection;
import java.util.Map;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class CollectionUtils {

    public static boolean isEmpty(@Nullable Collection<?> collection) {
        return (collection == null || collection.isEmpty());
    }

    public static boolean isEmpty(@Nullable Map<?, ?> map) {
        return (map == null || map.isEmpty());
    }

}