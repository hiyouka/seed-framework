package hiyouka.seedframework.beans.definition;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class BeanHolder {

    private Object bean;

    private String beanName;

    private Class<?> beanClass;

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }
}