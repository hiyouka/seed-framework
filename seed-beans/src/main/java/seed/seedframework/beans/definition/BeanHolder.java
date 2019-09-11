package seed.seedframework.beans.definition;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class BeanHolder<T> {

    private T bean;

    private String beanName;

    private Class<T> beanClass;

    public BeanHolder() {
    }

    public BeanHolder(T bean, String beanName) {
        this.bean = bean;
        this.beanName = beanName;
        if(bean != null){
            this.beanClass = (Class<T>) bean.getClass();
        }
    }

    public T getBean() {
        return bean;
    }

    public void setBean(T bean) {
        this.bean = bean;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public Class<T> getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class<T> beanClass) {
        this.beanClass = beanClass;
    }
}