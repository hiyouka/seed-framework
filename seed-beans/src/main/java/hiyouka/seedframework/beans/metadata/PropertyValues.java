package hiyouka.seedframework.beans.metadata;


import javax.annotation.Nullable;

/**
 * 对象属性的集合{@link PropertyValue}
 * @author hiyouka
 * @since JDK 1.8
 */
public interface PropertyValues {

    /**
     *  返回所有的属性集合
     */
    PropertyValue[] getPropertyValues();

    @Nullable
    PropertyValue getPropertyValue(String propertyName);

    boolean contains(String propertyName);

    boolean isEmpty();

}