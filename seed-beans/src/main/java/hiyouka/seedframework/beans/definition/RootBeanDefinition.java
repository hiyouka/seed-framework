package hiyouka.seedframework.beans.definition;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class RootBeanDefinition extends AbstractBeanDefinition {

    private BeanDefinitionHolder decoratedDefinition;

    public RootBeanDefinition(Class<?> clazz){
        super();
        setBeanClass(clazz);
    }

    public RootBeanDefinition(Class<?> clazz,String className){
        this(clazz);
        setBeanClassName(className);
    }

    public BeanDefinitionHolder getDecoratedDefinition() {
        return decoratedDefinition;
    }

    public void setDecoratedDefinition(BeanDefinitionHolder decoratedDefinition) {
        this.decoratedDefinition = decoratedDefinition;
    }

    @Override
    public void setParentName(String parentName) {
        if(parentName != null)
            throw new IllegalArgumentException("Root bean cannot be changed into a child bean with parent reference ");
    }

    @Override
    public String getParentName() {
        return null;
    }
}