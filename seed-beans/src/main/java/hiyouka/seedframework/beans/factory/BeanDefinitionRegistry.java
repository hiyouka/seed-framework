package hiyouka.seedframework.beans.factory;

import hiyouka.seedframework.beans.definition.BeanDefinition;
import hiyouka.seedframework.beans.exception.BeanDefinitionStoreException;
import hiyouka.seedframework.beans.exception.NoSuchBeanDefinitionException;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public interface BeanDefinitionRegistry {

    /**
     * register hiyouka.framework.test.bean
     * @param beanName  the hiyouka.framework.test.bean name
     * @param beanDefinition hiyouka.framework.test.bean Description
     * @throws BeanDefinitionStoreException if register beanName already exists
     */
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws BeanDefinitionStoreException;


    /**
     * @param beanName hiyouka.framework.test.bean name
     * @throws NoSuchBeanDefinitionException if not found this beanDefinition
     */
    void removeBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;

    /**
     * get beanDefinition from factory
     * @param beanName hiyouka.framework.test.bean name
     * @return {@link BeanDefinition}
     * @throws  NoSuchBeanDefinitionException if not found this beanDefinition
     */
    BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;

    /**
     * get all beanDefinition names
     * @return array of  beanDefinition hiyouka.framework.test.bean names
     */
    String[] getBeanDefinitionNames();

    int getBeanDefinitionCount();

    boolean containsBeanDefinition(String beanName);

    /**
     * @param beanName  hiyouka.framework.test.bean name
     * @return if have this beanName definition return true
     */
    boolean isBeanNameInUse(String beanName);

}