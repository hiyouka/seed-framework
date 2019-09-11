package seed.seedframework.util;

import java.io.Serializable;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/**
 * @author hiyouka
 * Date: 2019/2/22
 * @since JDK 1.8
 */
public class DefaultMultiValueMap<K, V> implements MultiValueMap<K, V>, Serializable,Cloneable {

    private static final long serialVersionUID = 3801124242820219131L;

    private final Map<K, List<V>> targetMap;

    public DefaultMultiValueMap(){
        this.targetMap = new LinkedHashMap<>();
    }

    public DefaultMultiValueMap(Map<K, List<V>> otherMap) {
        this.targetMap = new LinkedHashMap<>(otherMap);
    }

    @Override
    public void add(K key, V value){
        List<V> values = this.targetMap.get(key);
        if (values == null) {
            values = new LinkedList<>();
            this.targetMap.put(key, values);
        }
        values.add(value);
    }

    @Override
    public V getFirst(K key){
        List<V> values = this.targetMap.get(key);
        return (values != null ? values.get(0) : null);
    }

    @Override
    public int size() {
        return this.targetMap.size();
    }

    @Override
    public boolean isEmpty() {
        return this.targetMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return this.targetMap.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return this.targetMap.containsKey(value);
    }

    @Override
    public List<V> get(Object key) {
        return this.targetMap.get(key);
    }

    @Override
    public List<V> put(K key, List<V> value) {
        return this.targetMap.put(key,value);
    }

    @Override
    public List<V> remove(Object key) {
        return this.targetMap.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends List<V>> map) {
        this.targetMap.putAll(map);
    }

    @Override
    public List<V> getOrDefault(Object key, List<V> defaultValue) {
        return this.targetMap.getOrDefault(key,defaultValue);
    }

    @Override
    public void clear() {
        this.targetMap.clear();
    }



    @Override
    public Set<K> keySet() {
        return this.targetMap.keySet();
    }

    @Override
    public Collection<List<V>> values() {
        return this.targetMap.values();
    }

    @Override
    public Set<Map.Entry<K, List<V>>> entrySet() {
        return this.targetMap.entrySet();
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super List<V>> action) {
        targetMap.forEach(action);
    }

    @Override
    public void replaceAll(BiFunction<? super K, ? super List<V>, ? extends List<V>> function) {
        targetMap.replaceAll(function);
    }

    @Override
    public DefaultMultiValueMap<K, V> clone() {
        return new DefaultMultiValueMap<>(this);
    }

    @Override
    public boolean equals(Object obj) {
        return this.targetMap.equals(obj);
    }

    @Override
    public int hashCode() {
        return this.targetMap.hashCode();
    }

    @Override
    public String toString() {
        return this.targetMap.toString();
    }
}