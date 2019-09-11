package seed.seedframework.util;

import java.util.List;
import java.util.Map;

/**
 * @author hiyouka
 * Date: 2019/2/22
 * @since JDK 1.8
 */
public interface MultiValueMap <K, V> extends Map<K, List<V>> {

    void add(K key, V value);


    V getFirst(K key);

}