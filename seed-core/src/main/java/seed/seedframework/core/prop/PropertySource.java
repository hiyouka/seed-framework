package seed.seedframework.core.prop;

import seed.seedframework.util.Assert;

import java.util.Objects;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public abstract class PropertySource<T> {

    protected final String name;

    protected final T source;

    public PropertySource(String name, T source) {
        Assert.hasText(name,"property name not be null");
        this.name = name;
        this.source = source;
    }

    public PropertySource(String name){
        this(name, (T) new Object());
    }

    public String getName() {
        return name;
    }


    public T getSource() {
        return source;
    }


    public abstract Object getProperty(String name);

    public boolean containsProperty(String name) {
        return (getProperty(name) != null);
    }


    @Override
    public boolean equals(Object obj) {
        return obj == this || (obj instanceof PropertySource && ((PropertySource) obj).getName().equals(this.name) );
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}