package hiyouka.seedframework.beans.metadata;

import com.sun.istack.internal.Nullable;
import hiyouka.seedframework.util.StringUtils;

import java.util.*;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class MutablePropertyValues implements PropertyValues {

    private final List<PropertyValue> propertyValues;

    /**
     * 已处理过的属性
     */
    @Nullable
    private Set<String> processedProperties;

    public MutablePropertyValues() {
        this.propertyValues = new ArrayList<>(0);
    }

    public MutablePropertyValues(@Nullable List<PropertyValue> propertyValueList) {
        this.propertyValues =
                (propertyValueList != null ? propertyValueList : new ArrayList<>());
    }

    public MutablePropertyValues(@Nullable PropertyValues original) {
        // We can optimize this because it's all new:
        // There is no replacement of existing property values.
        if (original != null) {
            PropertyValue[] pvs = original.getPropertyValues();
            this.propertyValues = new ArrayList<>(pvs.length);
            for (PropertyValue pv : pvs) {
                this.propertyValues.add(new PropertyValue(pv));
            }
        }
        else {
            this.propertyValues = new ArrayList<>(0);
        }
    }

    public MutablePropertyValues(@Nullable Map<?, ?> original) {
        // We can optimize this because it's all new:
        // There is no replacement of existing property values.
        if (original != null) {
            this.propertyValues = new ArrayList<>(original.size());
            original.forEach((attrName, attrValue) -> this.propertyValues.add(
                    new PropertyValue(attrName.toString(), attrValue)));
        }
        else {
            this.propertyValues = new ArrayList<>(0);
        }
    }

    public List<PropertyValue> getPropertyValueList() {
        return this.propertyValues;
    }

    public int size() {
        return this.propertyValues.size();
    }

    @Override
    public PropertyValue[] getPropertyValues() {
        return this.propertyValues.toArray(new PropertyValue[this.propertyValues.size()]);
    }

    @Nullable
    @Override
    public PropertyValue getPropertyValue(String propertyName) {
        for (PropertyValue pv : this.propertyValues) {
            if (pv.getName().equals(propertyName)) {
                return pv;
            }
        }
        return null;
    }

    public MutablePropertyValues addPropertyValues(@Nullable Map<?, ?> other) {
        if (other != null) {
            other.forEach((attrName, attrValue) -> addPropertyValue(
                    new PropertyValue(attrName.toString(), attrValue)));
        }
        return this;
    }

    public MutablePropertyValues addPropertyValue(PropertyValue pv) {
        for (int i = 0; i < this.propertyValues.size(); i++) {
            PropertyValue currentPv = this.propertyValues.get(i);
            if (currentPv.getName().equals(pv.getName())) {
                setPropertyValueAt(pv, i);
                return this;
            }
        }
        this.propertyValues.add(pv);
        return this;
    }

    public void setPropertyValueAt(PropertyValue pv, int i) {
        this.propertyValues.set(i, pv);
    }

    @Nullable
    public Object get(String propertyName) {
        PropertyValue pv = getPropertyValue(propertyName);
        return (pv != null ? pv.getValue() : null);
    }

    public void removePropertyValue(String propertyName) {
        this.propertyValues.remove(getPropertyValue(propertyName));
    }

    public void removePropertyValue(PropertyValue pv) {
        this.propertyValues.remove(pv);
    }

    @Override
    public boolean contains(String propertyName) {
        return (getPropertyValue(propertyName) != null ||
                (this.processedProperties != null && this.processedProperties.contains(propertyName)));
    }

    public void registerProcessedProperty(String propertyName) {
        if (this.processedProperties == null) {
            this.processedProperties = new HashSet<>(4);
        }
        this.processedProperties.add(propertyName);
    }

    public void clearProcessedProperty(String propertyName) {
        if (this.processedProperties != null) {
            this.processedProperties.remove(propertyName);
        }
    }

    @Override
    public boolean isEmpty() {
        return this.processedProperties.isEmpty();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof MutablePropertyValues)) {
            return false;
        }
        MutablePropertyValues that = (MutablePropertyValues) other;
        return this.propertyValues.equals(that.propertyValues);
    }

    @Override
    public int hashCode() {
        return this.propertyValues.hashCode();
    }

    @Override
    public String toString() {
        PropertyValue[] pvs = getPropertyValues();
        StringBuilder sb = new StringBuilder("PropertyValues: length=").append(pvs.length);
        if (pvs.length > 0) {
            sb.append("; ").append(StringUtils.arrayToDelimitedString(pvs, "; "));
        }
        return sb.toString();
    }


}