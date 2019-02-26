package hiyouka.seedframework.beans.definition;

import hiyouka.seedframework.beans.metadata.AnnotationMetadata;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class AnnotatedGenericBeanDefinition implements AnnotatedBeanDefinition {

    @Override
    public AnnotationMetadata getMetadata() {
        return null;
    }

    @Override
    public boolean isPrimary() {
        return false;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    @Override
    public boolean isPrototype() {
        return false;
    }

    @Override
    public boolean isLazyInit() {
        return false;
    }

    @Override
    public void setBeanClassName(String beanClassName) {

    }

    @Override
    public String getBeanClassName() {
        return null;
    }

    @Override
    public void setBeanClass(Class<?> beanClass) {

    }

    @Override
    public Class<?> getBeanClass() {
        return null;
    }

    @Override
    public void setScope(String scope) {

    }

    @Override
    public String getScope() {
        return null;
    }

    @Override
    public void setSingleton(boolean primary) {

    }

    @Override
    public void setPrototype(boolean prototype) {

    }

    @Override
    public void setLazyInit(boolean lazyInit) {

    }

    @Override
    public void setAttribute(String name, Object value) {

    }

    @Override
    public Object getAttribute(String name) {
        return null;
    }

    @Override
    public Object removeAttribute(String name) {
        return null;
    }

    @Override
    public boolean hasAttribute(String name) {
        return false;
    }

    @Override
    public String[] attributeNames() {
        return new String[0];
    }
}