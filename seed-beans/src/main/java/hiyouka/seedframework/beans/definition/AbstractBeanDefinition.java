package hiyouka.seedframework.beans.definition;

import com.sun.istack.internal.Nullable;
import hiyouka.seedframework.beans.metadata.MutablePropertyValues;
import hiyouka.seedframework.io.resource.Resource;
import hiyouka.seedframework.util.Assert;
import hiyouka.seedframework.util.ObjectUtils;

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

    private boolean abstractFlag = false;


    /**
     * bean属性信息
     */
    @Nullable
    private MutablePropertyValues propertyValues;

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

    protected AbstractBeanDefinition() {
        this.propertyValues = null;
    }

    protected AbstractBeanDefinition( @Nullable MutablePropertyValues pvs) {
        this.propertyValues = pvs;
    }

    protected AbstractBeanDefinition(BeanDefinition original) {
        setParentName(original.getParentName());
        setBeanClassName(original.getBeanClassName());
        setScope(original.getScope());
        setAbstract(original.isAbstract());
        setLazyInit(original.isLazyInit());
        setFactoryBeanName(original.getFactoryBeanName());
        setFactoryMethodName(original.getFactoryMethodName());
        if (original instanceof AbstractBeanDefinition) {
            AbstractBeanDefinition originalAbd = (AbstractBeanDefinition) original;
            if (originalAbd.hasBeanClass()) {
                setBeanClass(originalAbd.getBeanClass());
            }
            if (originalAbd.hasPropertyValues()) {
                    setPropertyValues(new MutablePropertyValues(original.getPropertyValues()));
            }
            setPrimary(originalAbd.isPrimary());
            setInitMethodName(originalAbd.getInitMethodName());
            setDestroyMethodName(originalAbd.getDestroyMethodName());
            setResource(originalAbd.getResource());
        }else {
            setPropertyValues(new MutablePropertyValues(original.getPropertyValues()));
        }
    }


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


    public String getFactoryBeanName() {
        return factoryBeanName;
    }

    public void setFactoryBeanName(String factoryBeanName) {
        this.factoryBeanName = factoryBeanName;
    }

    public String getFactoryMethodName() {
        return factoryMethodName;
    }

    public void setFactoryMethodName(String factoryMethodName) {
        this.factoryMethodName = factoryMethodName;
    }

    public String getInitMethodName() {
        return initMethodName;
    }

    public void setInitMethodName(String initMethodName) {
        this.initMethodName = initMethodName;
    }

    public String getDestroyMethodName() {
        return destroyMethodName;
    }

    public void setDestroyMethodName(String destroyMethodName) {
        this.destroyMethodName = destroyMethodName;
    }

    public void setBeanClass(Object beanClass) {
        this.beanClass = beanClass;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    /**
     * 默认情况下为false 告诉beanFactory 需要实例该定义信息
     * @param abstractFlag 是否需要实例化
     */
    public void setAbstract(boolean abstractFlag) {
        this.abstractFlag = abstractFlag;
    }


    @Override
    public boolean isAbstract() {
        return this.abstractFlag;
    }


    @Override
    public MutablePropertyValues getPropertyValues() {
        if (this.propertyValues == null) {
            this.propertyValues = new MutablePropertyValues();
        }
        return this.propertyValues;
    }

    @Override
    public boolean hasPropertyValues() {
        return (this.propertyValues != null && !this.propertyValues.isEmpty());
    }

    public void setPropertyValues(MutablePropertyValues propertyValues) {
        this.propertyValues = propertyValues;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof AbstractBeanDefinition)) {
            return false;
        }

        AbstractBeanDefinition that = (AbstractBeanDefinition) other;

        if (!ObjectUtils.nullSafeEquals(getBeanClassName(), that.getBeanClassName())) return false;
        if (!ObjectUtils.nullSafeEquals(this.scope, that.scope)) return false;
        if (this.abstractFlag != that.abstractFlag) return false;
        if (this.lazyInit != that.lazyInit) return false;

//        if (this.autowireMode != that.autowireMode) return false;
//        if (this.dependencyCheck != that.dependencyCheck) return false;
//        if (!Arrays.equals(this.dependsOn, that.dependsOn)) return false;
//        if (this.autowireCandidate != that.autowireCandidate) return false;
//        if (!ObjectUtils.nullSafeEquals(this.qualifiers, that.qualifiers)) return false;
        if (this.primary != that.primary) return false;

//        if (this.nonPublicAccessAllowed != that.nonPublicAccessAllowed) return false;
//        if (this.lenientConstructorResolution != that.lenientConstructorResolution) return false;
//        if (!ObjectUtils.nullSafeEquals(this.constructorArgumentValues, that.constructorArgumentValues)) return false;
//        if (!ObjectUtils.nullSafeEquals(this.propertyValues, that.propertyValues)) return false;
//        if (!ObjectUtils.nullSafeEquals(this.methodOverrides, that.methodOverrides)) return false;

        if (!ObjectUtils.nullSafeEquals(this.factoryBeanName, that.factoryBeanName)) return false;
        if (!ObjectUtils.nullSafeEquals(this.factoryMethodName, that.factoryMethodName)) return false;
        if (!ObjectUtils.nullSafeEquals(this.initMethodName, that.initMethodName)) return false;
//        if (this.enforceInitMethod != that.enforceInitMethod) return false;
        if (!ObjectUtils.nullSafeEquals(this.destroyMethodName, that.destroyMethodName)) return false;
//        if (this.enforceDestroyMethod != that.enforceDestroyMethod) return false;

//        if (this.synthetic != that.synthetic) return false;
//        if (this.role != that.role) return false;

        return super.equals(other);
    }

}