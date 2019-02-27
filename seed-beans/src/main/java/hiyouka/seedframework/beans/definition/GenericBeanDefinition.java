package hiyouka.seedframework.beans.definition;

import com.sun.istack.internal.Nullable;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class GenericBeanDefinition extends AbstractBeanDefinition{

    @Nullable
    private String parentName;

    public GenericBeanDefinition() {
        super();
    }

    /**
     * Create a new GenericBeanDefinition as deep copy of the given
     * bean definition.
     * @param original the original bean definition to copy from
     */
    public GenericBeanDefinition(BeanDefinition original) {
        super(original);
    }

    @Override
    @Nullable
    public String getParentName() {
        return this.parentName;
    }

    @Override
    public void setParentName(@Nullable String parentName) {
        this.parentName = parentName;
    }


    @Override
    public boolean equals(Object other) {
        return (this == other || (other instanceof GenericBeanDefinition && super.equals(other)));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Generic bean");
        if (this.parentName != null) {
            sb.append(" with parent '").append(this.parentName).append("'");
        }
        sb.append(": ").append(super.toString());
        return sb.toString();
    }


}