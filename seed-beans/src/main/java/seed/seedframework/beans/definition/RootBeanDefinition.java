package seed.seedframework.beans.definition;

import java.lang.reflect.Member;
import java.util.List;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class RootBeanDefinition extends AbstractBeanDefinition {

    private BeanDefinitionHolder decoratedDefinition;

    private List<Member> members;

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

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

    @Override
    public void setParentName(String parentName) {
        if(parentName != null)
            throw new IllegalArgumentException("Root hiyouka.framework.test.bean cannot be changed into a child hiyouka.framework.test.bean with parent reference ");
    }

    @Override
    public String getParentName() {
        return null;
    }

    @Override
    public String toString() {
        return "RootBeanDefinition{" +
                "decoratedDefinition=" + decoratedDefinition +
                "class:" + getBeanClassName() +
                '}';
    }
}