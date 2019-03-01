package hiyouka.seedframework.beans.definition;

import com.sun.istack.internal.Nullable;
import hiyouka.seedframework.beans.attribute.AttributeAccessor;
import hiyouka.seedframework.beans.metadata.MutablePropertyValues;

/**
 * bean Description
 * {@link AnnotatedGenericBeanDefinition}
 * @author hiyouka
 * Date: 2019/1/27
 * @since JDK 1.8
 */
public interface BeanDefinition extends AttributeAccessor {

    /**
     *  scope metadata
     */
    public static final String SCOPE_SINGLETON = "singleton";
    public static final String SCOPE_PROTOTYPE = "prototype";

    /**
     *  role is mean this bean is register by user
     */
    int ROLE_SUPPORT = 0;

    /**
     *  role is mean this bean is mine,
     *  no relevance to the user
     */
    int ROLE_INFRASTRUCTURE = 2;

    boolean isPrimary();

    boolean isSingleton();

    boolean isPrototype();

    boolean isLazyInit();

    void setBeanClassName(@Nullable String beanClassName);

    String getBeanClassName();

    void setBeanClass(Class<?> beanClass);

    Class<?> getBeanClass();

    void setScope(String scope);

    String getScope();

    void setPrimary(boolean primary);

    void setSingleton(boolean singleton);

    void setPrototype(boolean prototype);

    void setLazyInit(boolean lazyInit);

    void setParentName(@Nullable String parentName);

    void setFactoryBeanName(@Nullable String factoryBeanName);

    @Nullable
    String getFactoryBeanName();

    void setFactoryMethodName(@Nullable String factoryMethodName);

    @Nullable
    String getFactoryMethodName();

    /**
     * 父类名称
     */
    @Nullable
    String getParentName();

    /**
     * Return whether this bean is "abstract", that is, not meant to be instantiated.
     * 是否需要实例化
     */
    boolean isAbstract();

    MutablePropertyValues getPropertyValues();

    default boolean hasPropertyValues() {
        return !getPropertyValues().isEmpty();
    }

}