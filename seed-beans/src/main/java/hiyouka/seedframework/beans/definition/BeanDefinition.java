package hiyouka.seedframework.beans.definition;

import hiyouka.seedframework.beans.attribute.AttributeAccessor;

/**
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

    void setBeanClassName(String beanClassName);

    String getBeanClassName();

    void setBeanClass(Class<?> beanClass);

    Class<?> getBeanClass();

    void setScope(String scope);

    String getScope();

    void setSingleton(boolean primary);

    void setPrototype(boolean prototype);

    void setLazyInit(boolean lazyInit);


}