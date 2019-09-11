package seed.seedframework.beans.definition;

import org.objectweb.asm.ClassReader;

import javax.annotation.Nullable;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class GenericBeanDefinition extends AbstractBeanDefinition{

    @Nullable
    private String parentName;

    private ClassReader classReader;

    public GenericBeanDefinition() {
        super();
    }

    /**
     * Create a new GenericBeanDefinition as deep copy of the given
     * hiyouka.framework.test.bean definition.
     * @param original the original hiyouka.framework.test.bean definition to copy from
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
        StringBuilder sb = new StringBuilder("Generic hiyouka.framework.test.bean");
        if (this.parentName != null) {
            sb.append(" with parent '").append(this.parentName).append("'");
        }
        sb.append(": ").append(super.toString());
        return sb.toString();
    }

    public ClassReader getClassReader() {
        return classReader;
    }

    public void setClassReader(ClassReader classReader) {
        this.classReader = classReader;
    }

}