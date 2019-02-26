package hiyouka.seedframework.beans.definition;

import com.sun.istack.internal.Nullable;
import hiyouka.seedframework.io.resource.Resource;
import hiyouka.seedframework.util.Assert;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public abstract class AbstractBeanDefinition implements BeanDefinition {

    public static final String SCOPE_DEFAULT = "";

    private volatile Object beanClass;

    private String scope = SCOPE_DEFAULT;

    private boolean lazyInit = false;

    private boolean primary = false;

    private Resource resource;

    /**
     * 用于@Bean注解的工厂类的创建对象方法
     */
    @Nullable
    private String factoryBeanName;

    @Nullable
    private String factoryMethodName;

    /**
     * 初始化 和 销毁 方法
     */
    @Nullable
    private String initMethodName;

    @Nullable
    private String destroyMethodName;


    @Override
    public boolean isPrimary() {
        return this.primary;
    }

    @Override
    public boolean isSingleton() {
        return SCOPE_SINGLETON.equals(scope) || SCOPE_DEFAULT.equals(scope);
    }

    @Override
    public boolean isPrototype() {
        return SCOPE_PROTOTYPE.equals(scope);
    }

    @Override
    public boolean isLazyInit() {
        return this.lazyInit;
    }

    @Override
    public void setBeanClassName(String beanClassName) {

    }

    @Override
    public String getBeanClassName() {
        Object beanClass = this.beanClass;
        if(beanClass instanceof Class){
            return ((Class<?>) beanClass). getName();
        }else{
            return (String)beanClass;
        }
    }

    @Override
    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    @Override
    public Class<?> getBeanClass() {
        Object beanClass = this.beanClass;
        Assert.notNull(beanClass,"No bean class bind on bean definition");
        if(!(beanClass instanceof Class)){
            throw new IllegalStateException(
                    "Bean class name [" + beanClass + "] has not been resolved into an actual Class");
        }
        return (Class<?>) beanClass;
    }

    @Override
    public void setScope(String scope) {
        this.scope = scope;
    }

    @Override
    public String getScope() {
        return this.scope;
    }

    @Override
    public void setSingleton(boolean primary) {
        this.scope = SCOPE_SINGLETON;
    }

    @Override
    public void setPrototype(boolean prototype) {
        this.scope = SCOPE_PROTOTYPE;
    }

    @Override
    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
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

    public boolean hasBeanClass(){
        return (this.beanClass instanceof Class);
    }
}