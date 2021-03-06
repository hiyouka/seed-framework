package seed.seedframework.beans.metadata;

import seed.seedframework.util.Assert;

import javax.annotation.Nullable;
import java.io.Serializable;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class PropertyValue implements Serializable{

    private String name;

    private Object value;

    public PropertyValue(String name, @Nullable Object value) {
        Assert.notNull(name, "Name must not be null");
        this.name = name;
        this.value = value;
    }

    public PropertyValue(PropertyValue original) {
        Assert.notNull(original, "Original must not be null");
        this.name = original.getName();
        this.value = original.getValue();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}